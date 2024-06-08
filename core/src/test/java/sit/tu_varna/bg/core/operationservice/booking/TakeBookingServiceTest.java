package sit.tu_varna.bg.core.operationservice.booking;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.api.exception.InvalidResourceException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.booking.take.TakeBookingRequest;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.entity.User;
import sit.tu_varna.bg.enums.BookingStatus;
import sit.tu_varna.bg.enums.TicketStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
class TakeBookingServiceTest {

    @Inject
    TakeBookingService takeBookingService;


    Booking mockBooking;

    Ticket mockTicket;

    User mockUser;

    UUID bookingId;
    UUID userId;
    UUID ticketId;

    @BeforeEach
    void setUp() {
        PanacheMock.mock(Booking.class);
        PanacheMock.mock(Ticket.class);
        PanacheMock.mock(User.class);

        // Create UUIDs for test data
        bookingId = UUID.randomUUID();
        userId = UUID.randomUUID();
        ticketId = UUID.randomUUID();

        // Setup mock User
        mockUser = mock(User.class);
        when(mockUser.getId()).thenReturn(userId);

        mockTicket = mock(Ticket.class);
        when(mockTicket.getId()).thenReturn(ticketId);
        when(mockTicket.getTicketStatus()).thenReturn(TicketStatus.BOOKED);
        when(mockTicket.getUser()).thenReturn(mockUser);

        // Setup mock Booking
        mockBooking = mock(Booking.class);
        when(mockBooking.getId()).thenReturn(bookingId);
        when(mockBooking.getStatus()).thenReturn(BookingStatus.AVAILABLE);
        when(mockBooking.getTickets()).thenReturn(List.of(mockTicket));


        // Mock the static methods
        when(Booking.findByIdOptional(bookingId)).thenReturn(Optional.of(mockBooking));
        when(User.findByIdOptional(userId)).thenReturn(Optional.of(mockUser));
    }

    @Test
    void process_BookingNotFound() {
        // Arrange
        TakeBookingRequest request = new TakeBookingRequest();
        request.setBookingId(UUID.randomUUID()); // Use an ID that does not exist
        request.setUserId(userId);

        when(Booking.findByIdOptional(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> takeBookingService.process(request));
    }

    @Test
    void process_UserNotFound() {
        // Arrange
        TakeBookingRequest request = new TakeBookingRequest();
        request.setBookingId(bookingId);
        request.setUserId(UUID.randomUUID()); // Use an ID that does not exist

        when(User.findByIdOptional(any(UUID.class))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> takeBookingService.process(request));
    }

    @Test
    void process_BookingNotAvailable() {
        // Arrange
        when(mockBooking.getStatus()).thenReturn(BookingStatus.CANCELLED);

        TakeBookingRequest request = new TakeBookingRequest();
        request.setBookingId(bookingId);
        request.setUserId(userId);

        // Act & Assert
        assertThrows(InvalidResourceException.class, () -> takeBookingService.process(request));
    }
}
