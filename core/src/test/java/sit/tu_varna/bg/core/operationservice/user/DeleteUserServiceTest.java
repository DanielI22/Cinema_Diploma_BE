package sit.tu_varna.bg.core.operationservice.user;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.user.delete.DeleteUserRequest;
import sit.tu_varna.bg.api.operation.user.delete.DeleteUserResponse;
import sit.tu_varna.bg.core.common.KeycloakService;
import sit.tu_varna.bg.entity.*;
import sit.tu_varna.bg.enums.BookingStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@QuarkusTest
public class DeleteUserServiceTest {

    @InjectMocks
    DeleteUserService deleteUserService;

    @Mock
    KeycloakService keycloakService;

    UUID userId;
    User user;
    Booking booking;
    Ticket ticket;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(User.class);
        PanacheMock.mock(Booking.class);
        PanacheMock.mock(Ticket.class);

        userId = UUID.randomUUID();
        UUID bookingId = UUID.randomUUID();
        UUID ticketId = UUID.randomUUID();

        ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setShowtimeSeat(new ShowtimeSeat());

        booking = new Booking();
        booking.setId(bookingId);
        booking.setTickets(List.of(ticket));

        user = mock(User.class);
        user.setId(userId);
        user.setEmail("test@example.com");
        user.setBookings(Set.of(booking));
    }

    @Test
    void process_SuccessfulUserDeletion() {
        // Arrange
        when(User.findByIdOptional(userId)).thenReturn(Optional.of(user));
        when(user.isPersistent()).thenReturn(true);

        DeleteUserRequest request = DeleteUserRequest.builder()
                .userId(userId)
                .build();

        // Act
        DeleteUserResponse response = deleteUserService.process(request);

        // Assert
        assertTrue(response.getDeleted());
        Set<Booking> bookings = user.getBookings();
        assertTrue(bookings.stream()
                .allMatch(b -> b.getStatus().equals(BookingStatus.CANCELLED)));
        assertTrue(bookings.stream()
                .flatMap(b -> b.getTickets().stream())
                .noneMatch(t -> t.getShowtimeSeat().isBooked()));
        verify(keycloakService, times(1)).deleteUser(userId.toString());
    }

    @Test
    void process_UserNotFound() {
        // Arrange
        when(User.findByIdOptional(userId)).thenReturn(Optional.empty());

        DeleteUserRequest request = DeleteUserRequest.builder()
                .userId(userId)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> deleteUserService.process(request));

        verify(keycloakService, never()).deleteUser(anyString());
    }
}
