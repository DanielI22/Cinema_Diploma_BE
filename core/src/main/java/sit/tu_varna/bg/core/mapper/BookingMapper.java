package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.BookingDto;
import sit.tu_varna.bg.core.externalservice.KeycloakService;
import sit.tu_varna.bg.core.interfaces.ObjectMapper;
import sit.tu_varna.bg.entity.Booking;

@ApplicationScoped
public class BookingMapper implements ObjectMapper {
    @Inject
    KeycloakService keycloakService;
    @Inject
    ShowtimeMapper showtimeMapper;

    public BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId().toString())
                .userMail(keycloakService.getUserRepresentation(booking.getUser().getId().toString()).getEmail())
                .showtime(showtimeMapper.showtimeToShowtimeDto(booking.getShowtime()))
                .build();
    }
}
