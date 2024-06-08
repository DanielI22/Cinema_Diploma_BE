package sit.tu_varna.bg.core.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.OperatorTicketDto;
import sit.tu_varna.bg.api.dto.SeatDto;
import sit.tu_varna.bg.api.dto.ShowtimeTicketDto;
import sit.tu_varna.bg.api.dto.TicketDto;
import sit.tu_varna.bg.entity.*;
import sit.tu_varna.bg.enums.TicketType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class TicketMapperTest {

    @InjectMocks
    TicketMapper ticketMapper;

    @Mock
    HallMapper hallMapper;

    Ticket ticket;
    Showtime showtime;
    Movie movie;
    Cinema cinema;
    Hall hall;
    Seat seat;
    ShowtimeSeat showtimeSeat;
    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID ticketId = UUID.randomUUID();
        UUID showtimeId = UUID.randomUUID();
        UUID cinemaId = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();
        UUID hallId = UUID.randomUUID();
        UUID seatId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .email("user@example.com")
                .build();

        cinema = Cinema.builder()
                .id(cinemaId)
                .name("Cinema Name")
                .build();

        movie = Movie.builder()
                .id(movieId)
                .title("Movie Title")
                .posterImageUrl("http://example.com/poster.jpg")
                .build();

        hall = Hall.builder()
                .id(hallId)
                .name("Hall Name")
                .build();

        seat = Seat.builder()
                .id(seatId)
                .seatNumber(10)
                .row(Row.builder().rowNumber(5).build())
                .build();

        showtime = Showtime.builder()
                .id(showtimeId)
                .cinema(cinema)
                .movie(movie)
                .hall(hall)
                .startTime(LocalDateTime.of(2023, 5, 30, 14, 0))
                .build();

        showtimeSeat = ShowtimeSeat.builder()
                .seat(seat)
                .showtime(showtime)
                .isBooked(true)
                .build();

        ticket = Ticket.builder()
                .id(ticketId)
                .shortCode("ABC123")
                .ticketType(TicketType.NORMAL)
                .price(BigDecimal.valueOf(12.50))
                .createdOn(Instant.now())
                .showtime(showtime)
                .showtimeSeat(showtimeSeat)
                .user(user)
                .build();

        when(hallMapper.seatToSeatDto(seat)).thenReturn(SeatDto.builder()
                .id(seatId.toString())
                .seatNumber(seat.getSeatNumber())
                .rowNumber(seat.getRow().getRowNumber())
                .isEmpty(seat.isEmptySpace())
                .isBooked(showtimeSeat.isBooked())
                .build());
    }

    @Test
    void testTicketToShowtimeTicketDto() {
        ShowtimeTicketDto dto = ticketMapper.ticketToShowtimeTicketDto(ticket);

        assertEquals(ticket.getId().toString(), dto.getId());
        assertEquals(ticket.getUser().getEmail(), dto.getUserMail());
        assertEquals(ticket.getTicketType().name().toLowerCase(), dto.getType());
        assertEquals(ticket.getPrice().doubleValue(), dto.getPrice());
        assertEquals(seat.getId().toString(), dto.getSeat().getId());
    }

    @Test
    void testTicketToTicketDto() {
        TicketDto dto = ticketMapper.ticketToTicketDto(ticket);

        assertEquals(ticket.getId().toString(), dto.getId());
        assertEquals(ticket.getShortCode(), dto.getShortcode());
        assertEquals(ticket.getShowtime().getMovie().getPosterImageUrl(), dto.getMoviePoster());
        assertEquals(ticket.getShowtime().getMovie().getTitle(), dto.getMovieTitle());
        assertEquals(ticket.getShowtime().getCinema().getName(), dto.getCinemaName());
        assertEquals(ticket.getShowtime().getHall().getName(), dto.getHallName());
        assertEquals(ticket.getShowtime().getStartTime(), dto.getShowtimeStartTime());
        assertEquals(ticket.getTicketType().name().toLowerCase(), dto.getType());
        assertEquals(ticket.getPrice().doubleValue(), dto.getPrice());
        assertEquals(seat.getId().toString(), dto.getSeat().getId());
    }

    @Test
    void testTicketToOperatorTicketDto() {
        OperatorTicketDto dto = ticketMapper.ticketToOperatorTicketDto(ticket);

        assertEquals(ticket.getId().toString(), dto.getId());
        assertEquals(ticket.getShortCode(), dto.getShortcode());
        assertEquals(ticket.getTicketType().name().toLowerCase(), dto.getType());
        assertEquals(ticket.getPrice().doubleValue(), dto.getPrice());
        assertEquals(ticket.getShowtime().getStartTime(), dto.getShowtimeStartTime());
        assertEquals(ticket.getShowtime().getMovie().getTitle(), dto.getMovieTitle());
        assertEquals(LocalDateTime.ofInstant(ticket.getCreatedOn(), ZoneId.systemDefault()), dto.getSoldTime());
    }
}
