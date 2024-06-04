package sit.tu_varna.bg.core.operationservice.payment;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.dto.PurchaseSeatDto;
import sit.tu_varna.bg.api.exception.InvalidResourceException;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.payment.PaymentOperation;
import sit.tu_varna.bg.api.operation.payment.PaymentRequest;
import sit.tu_varna.bg.api.operation.payment.PaymentResponse;
import sit.tu_varna.bg.core.common.PricingService;
import sit.tu_varna.bg.core.common.StripePaymentService;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.entity.ShowtimeSeat;
import sit.tu_varna.bg.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@ApplicationScoped
public class PaymentService implements PaymentOperation {
    @Inject
    StripePaymentService stripePaymentService;
    @Inject
    PricingService pricingService;

    @Override
    @Transactional
    public PaymentResponse process(PaymentRequest request) {
        UUID userId = request.getUserId();
        User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        UUID showtimeId = request.getOrderInfo().getShowtimeId();
        Showtime showtime = (Showtime) Showtime.findByIdOptional(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with id " + showtimeId + " not found"));

        if (LocalDateTime.now().isAfter(showtime.getStartTime().plusMinutes(showtime.getMovie().getDuration()))) {
            throw new InvalidResourceException("Showtime has ended");
        }

        for (PurchaseSeatDto bookingSeat : request.getOrderInfo().getSeats()) {
            ShowtimeSeat showtimeSeat = ShowtimeSeat.findBySeatIdAndShowtimeId(bookingSeat.getSeatId(), showtimeId)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid showtime seat"));

            if (showtimeSeat.isBooked()) {
                throw new ResourceAlreadyExistsException("Seat already booked");
            }
        }

        BigDecimal totalSum = pricingService.calculateOrderPrice(request.getOrderInfo().getSeats(), showtime.getTicketPrice());
        String paymentMethodId = request.getPaymentMethodId();
        String clientSecret = stripePaymentService.createPaymentIntent(totalSum.multiply(BigDecimal.valueOf(100)), paymentMethodId);

        Map<String, String> responseData = new HashMap<>();
        responseData.put("clientSecret", clientSecret);
        return PaymentResponse.builder().paymentResponse(responseData).build();
    }
}
