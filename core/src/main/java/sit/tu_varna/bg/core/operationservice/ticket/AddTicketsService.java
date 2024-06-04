package sit.tu_varna.bg.core.operationservice.ticket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sit.tu_varna.bg.api.dto.PurchaseSeatDto;
import sit.tu_varna.bg.api.exception.InvalidResourceException;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsResponse;
import sit.tu_varna.bg.core.common.EmailService;
import sit.tu_varna.bg.core.common.PricingService;
import sit.tu_varna.bg.core.common.ShortCodeGenerator;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.entity.ShowtimeSeat;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.entity.User;
import sit.tu_varna.bg.enums.TicketStatus;
import sit.tu_varna.bg.enums.TicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class AddTicketsService implements AddTicketsOperation {
    @Inject
    PricingService pricingService;
    @Inject
    EmailService emailService;
    @ConfigProperty(name = "client.link")
    String clientLink;

    @Override
    @Transactional
    public AddTicketsResponse process(AddTicketsRequest request) {
        UUID userId = request.getUserId();
        User user = (User) User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        UUID showtimeId = request.getOrderInfo().getShowtimeId();
        Showtime showtime = (Showtime) Showtime.findByIdOptional(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with id " + showtimeId + " not found"));

        if (LocalDateTime.now().isAfter(showtime.getStartTime().plusMinutes(showtime.getMovie().getDuration()))) {
            throw new InvalidResourceException("Showtime has ended");
        }

        Collection<UUID> ticketIds = new ArrayList<>();
        for (PurchaseSeatDto bookingSeat : request.getOrderInfo().getSeats()) {
            ShowtimeSeat showtimeSeat = ShowtimeSeat.findBySeatIdAndShowtimeId(bookingSeat.getSeatId(), showtimeId)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid showtime seat"));

            if (request.getOrderInfo().getBookingId() == null && showtimeSeat.isBooked()) {
                throw new ResourceAlreadyExistsException("Seat already booked");
            }

            showtimeSeat.setBooked(true);
            showtimeSeat.persist();

            String shortCode = ShortCodeGenerator.generateShortCode();
            while (Ticket.find("shortCode", shortCode).firstResult() != null) {
                shortCode = ShortCodeGenerator.generateShortCode();
            }
            TicketType ticketType = TicketType.valueOf(bookingSeat.getTicketType().name().toUpperCase(Locale.ROOT));
            BigDecimal ticketPrice = pricingService.calculateTicketPrice(ticketType, showtime.getTicketPrice());
            Ticket ticket = Ticket.builder()
                    .user(user)
                    .shortCode(shortCode)
                    .showtimeSeat(showtimeSeat)
                    .showtime(showtime)
                    .ticketType(ticketType)
                    .price(ticketPrice)
                    .ticketStatus(TicketStatus.PURCHASED)
                    .build();

            ticket.persist();
            ticketIds.add(ticket.getId());
        }
        try {
            String language = request.getOrderInfo().getLanguage();
            emailService.sendTicketConfirmationEmail(user.getEmail(), clientLink + "/my-tickets", language);
        } catch (Exception ignored) {
        }

        return AddTicketsResponse.builder().ticketIds(ticketIds.stream().map(UUID::toString).collect(Collectors.toList())).build();
    }
}
