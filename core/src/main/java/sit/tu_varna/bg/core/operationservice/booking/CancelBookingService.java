package sit.tu_varna.bg.core.operationservice.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingOperation;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingRequest;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingResponse;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.entity.ShowtimeSeat;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.enums.BookingStatus;
import sit.tu_varna.bg.repository.BookingRepository;

import java.util.UUID;

@ApplicationScoped
public class CancelBookingService implements CancelBookingOperation {
    @Inject
    BookingRepository bookingRepository;

    @Transactional
    @Override
    public CancelBookingResponse process(CancelBookingRequest request) {
        UUID bookingId = request.getBookingId();
        Booking booking = (Booking) bookingRepository.findByIdOptional(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking with id " + bookingId + " not found"));

        if (!booking.getStatus().equals(BookingStatus.AVAILABLE)) {
            throw new ResourceAlreadyExistsException("Booking is not available to be cancelled");
        }

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
