package sit.tu_varna.bg.core.operationservice.booking;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.BookingDto;
import sit.tu_varna.bg.api.exception.InvalidResourceException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.booking.validate.ValidateBookingRequest;
import sit.tu_varna.bg.api.operation.booking.validate.ValidateBookingResponse;
import sit.tu_varna.bg.core.mapper.BookingMapper;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@QuarkusTest
class ValidateBookingServiceTest {

    @InjectMocks
    ValidateBookingService validateBookingService;

    @Mock
    BookingMapper bookingMapper;

    private Booking booking;
    private BookingDto bookingDto;
    private ValidateBookingRequest request;
    private UUID cinemaId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        PanacheMock.mock(Booking.class);

        cinemaId = UUID.randomUUID();

        Cinema cinema = new Cinema();
        cinema.setId(cinemaId);

        Movie movie = new Movie();
        movie.setDuration(120); // Duration in minutes

        Showtime showtime = new Showtime();
        showtime.setId(UUID.randomUUID());
        showtime.setCinema(cinema);
        showtime.setMovie(movie);
        showtime.setStartTime(LocalDateTime.now().plusHours(1));

        booking = new Booking();
        booking.setId(UUID.randomUUID());
        booking.setShortCode("BOOK123");
        booking.setShowtime(showtime);
        booking.setStatus(BookingStatus.AVAILABLE);

        bookingDto = BookingDto.builder()
                .id(booking.getId().toString())
                .shortcode(booking.getShortCode())
                .cinemaName(cinema.getName())
                .build();

        request = ValidateBookingRequest.builder()
                .shortCode(booking.getShortCode())
                .cinemaId(cinemaId)
                .build();
    }

    @Test
    void process_ValidBooking() {
        // Arrange
        when(Booking.findByShortCode(booking.getShortCode())).thenReturn(Optional.of(booking));
        when(bookingMapper.bookingToBookingDto(booking)).thenReturn(bookingDto);

        // Act
        ValidateBookingResponse response = validateBookingService.process(request);

        // Assert
        assertEquals(bookingDto, response.getBooking());
    }

    @Test
    void process_InvalidBookingCode() {
        // Arrange
        when(Booking.findByShortCode("INVALID")).thenReturn(Optional.empty());

        ValidateBookingRequest invalidRequest = ValidateBookingRequest.builder()
                .shortCode("INVALID")
                .cinemaId(cinemaId)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> validateBookingService.process(invalidRequest));
    }

    @Test
    void process_WrongCinema() {
        // Arrange
        UUID wrongCinemaId = UUID.randomUUID();
        request.setCinemaId(wrongCinemaId);

        when(Booking.findByShortCode(booking.getShortCode())).thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> validateBookingService.process(request));
    }

    @Test
    void process_BookingCancelled() {
        // Arrange
        booking.setStatus(BookingStatus.CANCELLED);

        when(Booking.findByShortCode(booking.getShortCode())).thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(InvalidResourceException.class, () -> validateBookingService.process(request));
    }

    @Test
    void process_BookingExpired() {
        // Arrange
        booking.setStatus(BookingStatus.EXPIRED);

        when(Booking.findByShortCode(booking.getShortCode())).thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(InvalidResourceException.class, () -> validateBookingService.process(request));
    }

    @Test
    void process_BookingTaken() {
        // Arrange
        booking.setStatus(BookingStatus.TAKEN);

        when(Booking.findByShortCode(booking.getShortCode())).thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(InvalidResourceException.class, () -> validateBookingService.process(request));
    }

    @Test
    void process_BookingPastShowtime() {
        // Arrange
        booking.getShowtime().setStartTime(LocalDateTime.now().minusHours(1));

        when(Booking.findByShortCode(booking.getShortCode())).thenReturn(Optional.of(booking));

        // Act & Assert
        assertThrows(InvalidResourceException.class, () -> validateBookingService.process(request));
    }
}
