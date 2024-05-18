package sit.tu_varna.bg.core.operationservice.ticket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.dto.PurchaseSeatDto;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsResponse;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.core.externalservice.PricingService;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.entity.ShowtimeSeat;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.entity.User;
import sit.tu_varna.bg.enums.TicketStatus;
import sit.tu_varna.bg.enums.TicketType;

import java.math.BigDecimal;
import java.time.Duration;
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

    @Override
    @Transactional
    public AddTicketsResponse process(AddTicketsRequest request) {
        UUID userId = request.getUserId();
        User user = (User) User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        UUID showtimeId = request.getOrderInfo().getShowtimeId();
        Showtime showtime = (Showtime) Showtime.findByIdOptional(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with id " + showtimeId + " not found"));

        if (Duration.between(LocalDateTime.now(), showtime.getStartTime()).toMinutes() <= BusinessConstants.BOOKING_EXPIRE_TIME) {
            throw new ResourceAlreadyExistsException("Showtime has already started");
        }

        Collection<UUID> ticketIds = new ArrayList<>();
        for (PurchaseSeatDto bookingSeat : request.getOrderInfo().getSeats()) {
            ShowtimeSeat showtimeSeat = ShowtimeSeat.findBySeatIdAndShowtimeId(bookingSeat.getSeatId(), showtimeId)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid showtime seat"));

            if (showtimeSeat.isBooked()) {
                throw new ResourceAlreadyExistsException("Seat already booked");
            }

            showtimeSeat.setBooked(true);
            showtimeSeat.persist();

            TicketType ticketType = TicketType.valueOf(bookingSeat.getTicketType().name().toUpperCase(Locale.ROOT));
            BigDecimal ticketPrice = pricingService.calculateTicketPrice(ticketType, showtime.getTicketPrice());
            Ticket ticket = Ticket.builder()
                    .user(user)
                    .showtimeSeat(showtimeSeat)
                    .ticketType(ticketType)
                    .price(ticketPrice)
                    .ticketStatus(TicketStatus.PURCHASED)
                    .build();

            ticket.persist();
            ticketIds.add(ticket.getId());
        }

        return AddTicketsResponse.builder().ticketIds(ticketIds.stream().map(UUID::toString).collect(Collectors.toList())).build();
    }
}
