package sit.tu_varna.bg.core.operationservice.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.dto.BookingDto;
import sit.tu_varna.bg.api.operation.booking.getall.GetAllBookingsOperation;
import sit.tu_varna.bg.api.operation.booking.getall.GetAllBookingsRequest;
import sit.tu_varna.bg.api.operation.booking.getall.GetAllBookingsResponse;
import sit.tu_varna.bg.core.mapper.BookingMapper;
import sit.tu_varna.bg.entity.Booking;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetAllBookingsService implements GetAllBookingsOperation {
    @Inject
    BookingMapper bookingMapper;

    @Transactional
    @Override
    public GetAllBookingsResponse process(GetAllBookingsRequest request) {
        List<BookingDto> bookings = Booking.listAll()
                .stream()
                .filter(Booking.class::isInstance)
                .map(Booking.class::cast)
                .sorted(Comparator.comparing(Booking::getCreatedOn).reversed())
                .map(b -> bookingMapper.bookingToBookingDto(b))
                .collect(Collectors.toList());

        return GetAllBookingsResponse.builder()
                .bookings(bookings)
                .build();
    }
}
