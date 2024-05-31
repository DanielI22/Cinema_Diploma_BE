package sit.tu_varna.bg.core.operationservice.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.exception.InvalidResourceException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.booking.validate.ValidateBookingOperation;
import sit.tu_varna.bg.api.operation.booking.validate.ValidateBookingRequest;
import sit.tu_varna.bg.api.operation.booking.validate.ValidateBookingResponse;
import sit.tu_varna.bg.core.mapper.BookingMapper;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.enums.BookingStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ApplicationScoped
public class ValidateBookingService implements ValidateBookingOperation {
    @Inject
    BookingMapper bookingMapper;

    @Override
    public ValidateBookingResponse process(ValidateBookingRequest request) {
        String shortcode = request.getShortCode();
        Booking booking = Booking.findByShortCode(shortcode)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid booking code."));


        if (!booking.getShowtime().getCinema().getId().equals(request.getCinemaId())) {
            throw new ResourceNotFoundException("Invalid booking code.");
        }
        // Check booking status
        if (booking.getStatus().equals(BookingStatus.CANCELLED)) {
            throw new InvalidResourceException("Booking has been cancelled.");
        }

        if (booking.getStatus().equals(BookingStatus.EXPIRED)) {
            throw new InvalidResourceException("Booking has expired.");
        }

        if (booking.getStatus().equals(BookingStatus.TAKEN)) {
            throw new InvalidResourceException("Booking has already been taken.");
        }

        // Check if the booking is for a future date and time
        if (booking.getShowtime().getStartTime().isBefore(LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault()))) {
            throw new InvalidResourceException("Invalid booking.");
        }
        return ValidateBookingResponse.builder().booking(bookingMapper.bookingToBookingDto(booking)).build();
    }
}
