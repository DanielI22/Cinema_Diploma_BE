package sit.tu_varna.bg.core.operationservice.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.BookingDto;
import sit.tu_varna.bg.api.operation.booking.getmybookings.GetMyBookingsOperation;
import sit.tu_varna.bg.api.operation.booking.getmybookings.GetMyBookingsRequest;
import sit.tu_varna.bg.api.operation.booking.getmybookings.GetMyBookingsResponse;
import sit.tu_varna.bg.core.mapper.BookingMapper;
import sit.tu_varna.bg.entity.Booking;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetMyBookingsService implements GetMyBookingsOperation {
    @Inject
    BookingMapper bookingMapper;

    @Override
    public GetMyBookingsResponse process(GetMyBookingsRequest request) {
        List<BookingDto> bookings = Booking.findByUserId(request.getUserId())
                .stream()
                .sorted(Comparator.comparing(Booking::getCreatedOn).reversed())
                .map(bookingMapper::bookingToBookingDto)
                .collect(Collectors.toList());

        return GetMyBookingsResponse.builder()
                .bookings(bookings)
                .build();
    }
}
