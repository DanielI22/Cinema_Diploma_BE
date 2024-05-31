package sit.tu_varna.bg.core.operationservice.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.InvalidResourceException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.booking.take.TakeBookingOperation;
import sit.tu_varna.bg.api.operation.booking.take.TakeBookingRequest;
import sit.tu_varna.bg.api.operation.booking.take.TakeBookingResponse;
import sit.tu_varna.bg.core.common.ShortCodeGenerator;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.entity.User;
import sit.tu_varna.bg.enums.BookingStatus;
import sit.tu_varna.bg.enums.TicketStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@ApplicationScoped
public class TakeBookingService implements TakeBookingOperation {

    @Override
    @Transactional
    public TakeBookingResponse process(TakeBookingRequest request) {
        Booking booking = (Booking) Booking.findByIdOptional(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking does not exist"));

        User user = (User) User.findByIdOptional(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));

        if (!booking.getStatus().equals(BookingStatus.AVAILABLE)) {
            throw new InvalidResourceException("Booking is not available for taking");
        }

        booking.setStatus(BookingStatus.TAKEN);
        Collection<UUID> ticketIds = new ArrayList<>();

        for (Ticket ticket : booking.getTickets()) {
            String shortCode = ShortCodeGenerator.generateShortCode();
            while (Ticket.find("shortCode", shortCode).firstResult() != null) {
                shortCode = ShortCodeGenerator.generateShortCode();
            }
            ticket.setShortCode(shortCode);
            ticket.setTicketStatus(TicketStatus.PURCHASED);
            ticket.setCreatedOn(Instant.now());
            ticket.setUser(user);
            ticket.persist();
            ticketIds.add(ticket.getId());
        }

        booking.persist();

        return TakeBookingResponse.builder().ticketIds(ticketIds).build();
    }
}
