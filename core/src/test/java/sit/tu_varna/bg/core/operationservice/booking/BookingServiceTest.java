package sit.tu_varna.bg.core.operationservice.booking;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.api.dto.OrderInfoDto;
import sit.tu_varna.bg.api.dto.PurchaseSeatDto;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.booking.book.BookingRequest;
import sit.tu_varna.bg.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@QuarkusTest
class BookingServiceTest {

    @Inject
    BookingService bookingService;

    User user;
    Showtime showtime;
    Seat seat;
    ShowtimeSeat showtimeSeat;
    PurchaseSeatDto purchaseSeatDto;
    BookingRequest bookingRequest;

    @BeforeEach
    void setUp() {
        // Mock Panache entities
        PanacheMock.mock(User.class);
        PanacheMock.mock(Showtime.class);
        PanacheMock.mock(ShowtimeSeat.class);
        PanacheMock.mock(Ticket.class);

        // Initialize mock entities
        UUID userId = UUID.randomUUID();
        UUID showtimeId = UUID.randomUUID();
        UUID seatId = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");

        showtime = new Showtime();
        showtime.setId(showtimeId);
        showtime.setStartTime(LocalDateTime.now().plusHours(1));
        showtime.setTicketPrice(BigDecimal.TEN);

        seat = new Seat();
        seat.setId(seatId);
        seat.setSeatNumber(1);

        showtimeSeat = new ShowtimeSeat();
        showtimeSeat.setId(UUID.randomUUID());
        showtimeSeat.setSeat(seat);
        showtimeSeat.setShowtime(showtime);
        showtimeSeat.setBooked(false);

        purchaseSeatDto = new PurchaseSeatDto();
        purchaseSeatDto.setSeatId(seat.getId());
        purchaseSeatDto.setTicketType(PurchaseSeatDto.TicketType.normal);

        OrderInfoDto orderInfoDto = new OrderInfoDto();
        orderInfoDto.setShowtimeId(showtime.getId());
        orderInfoDto.setLanguage("EN");
        orderInfoDto.setSeats(List.of(purchaseSeatDto));

        bookingRequest = new BookingRequest();
        bookingRequest.setUserId(user.getId());
        bookingRequest.setOrderInfo(orderInfoDto);
    }

    @Test
    void process_UserNotFound() {
        // Arrange
        when(User.findByIdOptional(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookingService.process(bookingRequest));
    }

    @Test
    void process_ShowtimeNotFound() {
        // Arrange
        when(User.findByIdOptional(user.getId())).thenReturn(Optional.of(user));
        when(Showtime.findByIdOptional(showtime.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> bookingService.process(bookingRequest));
    }

    @Test
    void process_ShowtimeAlreadyStarted() {
        // Arrange
        showtime.setStartTime(LocalDateTime.now().minusMinutes(1));
        when(User.findByIdOptional(user.getId())).thenReturn(Optional.of(user));
        when(Showtime.findByIdOptional(showtime.getId())).thenReturn(Optional.of(showtime));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> bookingService.process(bookingRequest));
    }

    @Test
    void process_ExceedsMaximumSeats() {
        // Arrange
        PurchaseSeatDto extraSeat = new PurchaseSeatDto();
        extraSeat.setSeatId(UUID.randomUUID());
        extraSeat.setTicketType(PurchaseSeatDto.TicketType.normal);

        OrderInfoDto orderInfoDto = new OrderInfoDto();
        orderInfoDto.setShowtimeId(showtime.getId());
        orderInfoDto.setLanguage("EN");
        orderInfoDto.setSeats(List.of(purchaseSeatDto, extraSeat, extraSeat, extraSeat, extraSeat, extraSeat));
        bookingRequest.setOrderInfo(orderInfoDto);

        when(User.findByIdOptional(user.getId())).thenReturn(Optional.of(user));
        when(Showtime.findByIdOptional(showtime.getId())).thenReturn(Optional.of(showtime));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> bookingService.process(bookingRequest));
    }
}
