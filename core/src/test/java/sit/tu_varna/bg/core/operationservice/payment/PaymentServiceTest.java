package sit.tu_varna.bg.core.operationservice.payment;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.OrderInfoDto;
import sit.tu_varna.bg.api.dto.PurchaseSeatDto;
import sit.tu_varna.bg.api.exception.InvalidResourceException;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.payment.PaymentRequest;
import sit.tu_varna.bg.api.operation.payment.PaymentResponse;
import sit.tu_varna.bg.core.common.PricingService;
import sit.tu_varna.bg.core.common.StripePaymentService;
import sit.tu_varna.bg.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
class PaymentServiceTest {

    @InjectMocks
    PaymentService paymentService;

    @Mock
    StripePaymentService stripePaymentService;

    @Mock
    PricingService pricingService;

    User user;
    Showtime showtime;
    Seat seat;
    ShowtimeSeat showtimeSeat;
    PurchaseSeatDto purchaseSeatDto;
    PaymentRequest paymentRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(User.class);
        PanacheMock.mock(Showtime.class);
        PanacheMock.mock(ShowtimeSeat.class);

        // Initialize the entities
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
        showtime.setMovie(new Movie());
        showtime.getMovie().setDuration(120); // 2 hours

        seat = new Seat();
        seat.setId(seatId);
        seat.setSeatNumber(1);

        showtimeSeat = new ShowtimeSeat();
        showtimeSeat.setId(UUID.randomUUID());
        showtimeSeat.setSeat(seat);
        showtimeSeat.setShowtime(showtime);
        showtimeSeat.setBooked(false);

        purchaseSeatDto = new PurchaseSeatDto();
        purchaseSeatDto.setSeatId(seatId);
        purchaseSeatDto.setTicketType(PurchaseSeatDto.TicketType.normal);

        OrderInfoDto orderInfoDto = new OrderInfoDto();
        orderInfoDto.setShowtimeId(showtimeId);
        orderInfoDto.setLanguage("EN");
        orderInfoDto.setSeats(List.of(purchaseSeatDto));

        paymentRequest = new PaymentRequest();
        paymentRequest.setUserId(userId);
        paymentRequest.setOrderInfo(orderInfoDto);
        paymentRequest.setPaymentMethodId("pm_12345");
    }

    @Test
    void process_SuccessfulPayment() {
        // Arrange
        when(User.findByIdOptional(user.getId())).thenReturn(Optional.of(user));
        when(Showtime.findByIdOptional(showtime.getId())).thenReturn(Optional.of(showtime));
        when(ShowtimeSeat.findBySeatIdAndShowtimeId(seat.getId(), showtime.getId())).thenReturn(List.of(showtimeSeat));
        when(pricingService.calculateOrderPrice(anyList(), any())).thenReturn(BigDecimal.valueOf(100));
        when(stripePaymentService.createPaymentIntent(any(), any())).thenReturn("client_secret_12345");

        // Act
        PaymentResponse response = paymentService.process(paymentRequest);

        // Assert
        assertEquals("client_secret_12345", response.getPaymentResponse().get("clientSecret"));
        verify(stripePaymentService, times(1)).createPaymentIntent(BigDecimal.valueOf(10000), "pm_12345");
    }

    @Test
    void process_UserNotFound() {
        // Arrange
        when(User.findByIdOptional(user.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> paymentService.process(paymentRequest));
    }

    @Test
    void process_ShowtimeNotFound() {
        // Arrange
        when(User.findByIdOptional(user.getId())).thenReturn(Optional.of(user));
        when(Showtime.findByIdOptional(showtime.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> paymentService.process(paymentRequest));
    }

    @Test
    void process_ShowtimeEnded() {
        // Arrange
        showtime.setStartTime(LocalDateTime.now().minusHours(3)); // Showtime ended 3 hours ago
        when(User.findByIdOptional(user.getId())).thenReturn(Optional.of(user));
        when(Showtime.findByIdOptional(showtime.getId())).thenReturn(Optional.of(showtime));

        // Act & Assert
        assertThrows(InvalidResourceException.class, () -> paymentService.process(paymentRequest));
    }

    @Test
    void process_SeatAlreadyBooked() {
        // Arrange
        showtimeSeat.setBooked(true);
        when(User.findByIdOptional(user.getId())).thenReturn(Optional.of(user));
        when(Showtime.findByIdOptional(showtime.getId())).thenReturn(Optional.of(showtime));
        when(ShowtimeSeat.findBySeatIdAndShowtimeId(seat.getId(), showtime.getId())).thenReturn(List.of(showtimeSeat));

        // Act & Assert
        assertThrows(ResourceAlreadyExistsException.class, () -> paymentService.process(paymentRequest));
    }
}
