package sit.tu_varna.bg.core.operationservice.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.InvalidResourceException;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.booking.cancelmy.CancelMyBookingOperation;
import sit.tu_varna.bg.api.operation.booking.cancelmy.CancelMyBookingRequest;
import sit.tu_varna.bg.api.operation.booking.cancelmy.CancelMyBookingResponse;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.entity.ShowtimeSeat;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.enums.BookingStatus;

import java.util.UUID;

@ApplicationScoped
public class CancelMyBookingService implements CancelMyBookingOperation {

    @Transactional
    @Override
    public CancelMyBookingResponse process(CancelMyBookingRequest request) {
        UUID bookingId = request.getBookingId();
        Booking booking = (Booking) Booking.findByIdOptional(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + bookingId + " not found"));

        if (!booking.getStatus().equals(BookingStatus.AVAILABLE)) {
            throw new ResourceAlreadyExistsException("Booking is not available to be cancelled");
        }

        if (!booking.getUser().getId().equals(request.getUserId())) {
            throw new InvalidResourceException("Cannot cancel booking that is not yours");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        for (Ticket ticket : booking.getTickets()) {
            ShowtimeSeat showtimeSeat = ticket.getShowtimeSeat();
            showtimeSeat.setBooked(false);
            showtimeSeat.persist();
        }

        booking.persist();
        return CancelMyBookingResponse.builder().cancelled(true).build();
    }
}
