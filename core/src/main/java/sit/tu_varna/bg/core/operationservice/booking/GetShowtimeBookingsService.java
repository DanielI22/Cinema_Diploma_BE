package sit.tu_varna.bg.core.operationservice.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.ShowtimeBookingDto;
import sit.tu_varna.bg.api.operation.booking.getshowtime.GetShowtimeBookingsOperation;
import sit.tu_varna.bg.api.operation.booking.getshowtime.GetShowtimeBookingsRequest;
import sit.tu_varna.bg.api.operation.booking.getshowtime.GetShowtimeBookingsResponse;
import sit.tu_varna.bg.core.mapper.BookingMapper;
import sit.tu_varna.bg.entity.Booking;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetShowtimeBookingsService implements GetShowtimeBookingsOperation {
    @Inject
    BookingMapper bookingMapper;

    @Override
    public GetShowtimeBookingsResponse process(GetShowtimeBookingsRequest request) {
        List<ShowtimeBookingDto> bookings = Booking.findByShowtimeId(request.getShowtimeId())
                .stream()
                .sorted(Comparator.comparing(Booking::getCreatedOn).reversed())
                .map(b -> bookingMapper.bookingToShowtimeBookingDto(b))
                .collect(Collectors.toList());

        return GetShowtimeBookingsResponse.builder()
                .bookings(bookings)
                .build();
    }
}
