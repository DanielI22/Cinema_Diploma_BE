package sit.tu_varna.bg.core.operationservice.booking;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingRequest;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.entity.ShowtimeSeat;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.enums.BookingStatus;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@QuarkusTest
class CancelBookingServiceTest {

    @Inject
    CancelBookingService cancelBookingService;

    Booking booking;
    Ticket ticket;
    ShowtimeSeat showtimeSeat;

    @BeforeEach
    void setUp() {
        PanacheMock.mock(Booking.class);
        PanacheMock.mock(ShowtimeSeat.class);

        UUID bookingId = UUID.randomUUID();
        UUID ticketId = UUID.randomUUID();
        UUID showtimeSeatId = UUID.randomUUID();

        showtimeSeat = new ShowtimeSeat();
        showtimeSeat.setId(showtimeSeatId);
        showtimeSeat.setBooked(true);

        ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setShowtimeSeat(showtimeSeat);

        booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus(BookingStatus.AVAILABLE);
        booking.setTickets(Collections.singletonList(ticket));
    }

    @Test
    void process_BookingNotFound() {
        // Arrange
        CancelBookingRequest request = new CancelBookingRequest();
        request.setBookingId(UUID.randomUUID());

        when(Booking.findByIdOptional(request.getBookingId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> cancelBookingService.process(request));
    }

    @Test
    void process_BookingNotAvailable() {
        // Arrange
        CancelBookingRequest request = new CancelBookingRequest();
        request.setBookingId(booking.getId());
        booking.setStatus(BookingStatus.CANCELLED);

        when(Booking.findByIdOptional(booking.getId())).thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> cancelBookingService.process(request));
    }
}
