package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.api.dto.ShowtimeDto;
import sit.tu_varna.bg.entity.Showtime;

@ApplicationScoped
public class ShowtimeMapper {

    public ShowtimeDto showtimeToShowtimeDto(Showtime showtime) {
        return ShowtimeDto.builder()
                .id(showtime.getId().toString())
                .cinemaName(showtime.getCinema().getName())
                .movieName(showtime.getMovie().getTitle())
                .movieId(showtime.getMovie().getId().toString())
                .hallName(showtime.getHall().getName())
                .startTime(showtime.getStartTime())
                .ticketPrice(showtime.getTicketPrice().doubleValue())
                .build();
    }
}
