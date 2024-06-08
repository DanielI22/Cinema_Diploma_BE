package sit.tu_varna.bg.core.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.BookingDto;
import sit.tu_varna.bg.api.dto.ShowtimeBookingDto;
import sit.tu_varna.bg.api.dto.ShowtimeTicketDto;
import sit.tu_varna.bg.entity.*;
import sit.tu_varna.bg.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class BookingMapperTest {

    @Mock
    TicketMapper ticketMapper;

    @InjectMocks
    BookingMapper bookingMapper;

    private Booking booking;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UUID bookingId = UUID.randomUUID();
        UUID showtimeId = UUID.randomUUID();
        UUID ticketId = UUID.randomUUID();
        Showtime showtime = new Showtime();
        showtime.setId(showtimeId);
        Movie movie = new Movie();
        movie.setPosterImageUrl("posterUrl");
        movie.setTitle("Movie Title");
        showtime.setMovie(movie);
        Cinema cinema = new Cinema();
        cinema.setName("Cinema Name");
        showtime.setCinema(cinema);
        Hall hall = new Hall();
        hall.setName("Hall Name");
        showtime.setHall(hall);
        showtime.setStartTime(LocalDateTime.now());

        User user = new User();
        user.setEmail("user@example.com");

        Ticket ticket = new Ticket();
        ticket.setId(ticketId);
        ticket.setPrice(BigDecimal.valueOf(15.00));

        booking = new Booking();
        booking.setId(bookingId);
        booking.setShortCode("12345");
        booking.setShowtime(showtime);
        booking.setStatus(BookingStatus.TAKEN);
        booking.setTickets(List.of(ticket));
        booking.setUser(user);

        when(ticketMapper.ticketToShowtimeTicketDto(ticket)).thenReturn(ShowtimeTicketDto.builder().build());
    }

    @Test
    void testBookingToBookingDto() {
        BookingDto bookingDto = bookingMapper.bookingToBookingDto(booking);

        assertEquals(booking.getId().toString(), bookingDto.getId());
        assertEquals(booking.getShortCode(), bookingDto.getShortcode());
        assertEquals(booking.getShowtime().getMovie().getPosterImageUrl(), bookingDto.getMoviePoster());
        assertEquals(booking.getShowtime().getMovie().getTitle(), bookingDto.getMovieTitle());
        assertEquals(booking.getShowtime().getCinema().getName(), bookingDto.getCinemaName());
        assertEquals(booking.getShowtime().getHall().getName(), bookingDto.getHallName());
        assertEquals(booking.getShowtime().getId().toString(), bookingDto.getShowtimeId());
        assertEquals(booking.getShowtime().getStartTime(), bookingDto.getShowtimeStartTime());
        assertEquals(booking.getStatus().name().toLowerCase(), bookingDto.getStatus());
        assertEquals(booking.getTickets().size(), bookingDto.getTickets().size());
        assertEquals(booking.getTickets().stream().map(Ticket::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue(), bookingDto.getTotalPrice());
    }

    @Test
    void testBookingToShowtimeBookingDto() {
        ShowtimeBookingDto showtimeBookingDto = bookingMapper.bookingToShowtimeBookingDto(booking);

        assertEquals(booking.getId().toString(), showtimeBookingDto.getId());
        assertEquals(booking.getUser().getEmail(), showtimeBookingDto.getUserMail());
        assertEquals(booking.getStatus().name().toLowerCase(), showtimeBookingDto.getStatus());
        assertEquals(booking.getTickets().size(), showtimeBookingDto.getTickets().size());
    }
}
