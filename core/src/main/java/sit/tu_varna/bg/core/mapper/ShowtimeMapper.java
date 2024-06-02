package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.api.dto.ShowtimeDto;
import sit.tu_varna.bg.core.interfaces.ObjectMapper;
import sit.tu_varna.bg.entity.Showtime;

@ApplicationScoped
public class ShowtimeMapper implements ObjectMapper {

    public ShowtimeDto showtimeToShowtimeDto(Showtime showtime) {
        return ShowtimeDto.builder()
                .id(showtime.getId().toString())
                .cinemaName(showtime.getCinema().getName())
                .movieName(showtime.getMovie().getTitle())
                .movieDuration(showtime.getMovie().getDuration())
                .movieId(showtime.getMovie().getId().toString())
                .hallName(showtime.getHall().getName())
                .startTime(showtime.getStartTime())
                .type(showtime.getType().toString())
                .ticketPrice(showtime.getTicketPrice().doubleValue())
                .isCurrent(showtime.isCurrent())
                .isEnded(showtime.isEnded())
                .build();
    }
}
