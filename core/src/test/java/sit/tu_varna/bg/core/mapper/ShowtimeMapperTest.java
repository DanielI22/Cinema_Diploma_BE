package sit.tu_varna.bg.core.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.ShowtimeDto;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.enums.ShowtimeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ShowtimeMapperTest {

    @InjectMocks
    ShowtimeMapper showtimeMapper;

    Showtime showtime;
    Cinema cinema;
    Movie movie;
    Hall hall;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID showtimeId = UUID.randomUUID();
        UUID cinemaId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();
        UUID hallId = UUID.randomUUID();

        cinema = Cinema.builder()
                .id(cinemaId)
                .name("Cinema Name")
                .build();

        movie = Movie.builder()
                .id(movieId)
                .title("Movie Title")
                .duration(120)
                .build();

        hall = Hall.builder()
                .id(hallId)
                .name("Hall Name")
                .build();

        showtime = Showtime.builder()
                .id(showtimeId)
                .cinema(cinema)
                .movie(movie)
                .hall(hall)
                .startTime(LocalDateTime.of(2023, 5, 30, 14, 0))
                .type(ShowtimeType.TWO_D)
                .ticketPrice(BigDecimal.valueOf(12.50))
                .isCurrent(true)
                .isEnded(false)
                .build();
    }

    @Test
    void testShowtimeToShowtimeDto() {
        ShowtimeDto showtimeDto = showtimeMapper.showtimeToShowtimeDto(showtime);

        assertEquals(showtime.getId().toString(), showtimeDto.getId());
        assertEquals(showtime.getCinema().getName(), showtimeDto.getCinemaName());
        assertEquals(showtime.getMovie().getTitle(), showtimeDto.getMovieName());
        assertEquals(showtime.getMovie().getDuration(), showtimeDto.getMovieDuration());
        assertEquals(showtime.getMovie().getId().toString(), showtimeDto.getMovieId());
        assertEquals(showtime.getHall().getName(), showtimeDto.getHallName());
        assertEquals(showtime.getStartTime(), showtimeDto.getStartTime());
        assertEquals(showtime.getType().toString(), showtimeDto.getType());
        assertEquals(showtime.getTicketPrice().doubleValue(), showtimeDto.getTicketPrice());
        assertEquals(showtime.isCurrent(), showtimeDto.getIsCurrent());
        assertEquals(showtime.isEnded(), showtimeDto.getIsEnded());
    }
}
