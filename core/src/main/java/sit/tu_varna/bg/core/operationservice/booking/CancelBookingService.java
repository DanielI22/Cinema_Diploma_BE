package sit.tu_varna.bg.core.operationservice.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingOperation;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingRequest;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingResponse;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.entity.ShowtimeSeat;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.enums.BookingStatus;

@ApplicationScoped
public class CancelBookingService implements CancelBookingOperation {

    @Transactional
    @Override
    public CancelBookingResponse process(CancelBookingRequest request) {
        Booking booking = (Booking) Booking.findByIdOptional(request.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking does not exist"));

        booking.setStatus(BookingStatus.CANCELLED);

        for (Ticket ticket : booking.getTickets()) {
            ShowtimeSeat showtimeSeat = ticket.getShowtimeSeat();
            showtimeSeat.setBooked(false);
            showtimeSeat.persist();
        }

        booking.persist();
        return CancelBookingResponse.builder().cancelled(true).build();
    }
}
