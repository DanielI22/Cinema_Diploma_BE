package sit.tu_varna.bg.core.operationservice.ticket;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.TicketDto;
import sit.tu_varna.bg.api.exception.InvalidResourceException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.ticket.validate.ValidateTicketRequest;
import sit.tu_varna.bg.api.operation.ticket.validate.ValidateTicketResponse;
import sit.tu_varna.bg.core.mapper.TicketMapper;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.enums.TicketStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@QuarkusTest
class ValidateTicketServiceTest {

    @InjectMocks
    ValidateTicketService validateTicketService;

    @Mock
    TicketMapper ticketMapper;

    private Ticket ticket;
    private TicketDto ticketDto;
    private ValidateTicketRequest request;
    private UUID cinemaId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        PanacheMock.mock(Ticket.class);

        cinemaId = UUID.randomUUID();

        Cinema cinema = new Cinema();
        cinema.setId(cinemaId);

        Movie movie = new Movie();
        movie.setDuration(120); // Duration in minutes

        Showtime showtime = new Showtime();
        showtime.setId(UUID.randomUUID());
        showtime.setCinema(cinema);
        showtime.setMovie(movie);
        showtime.setStartTime(LocalDateTime.now().plusMinutes(15));

        ticket = new Ticket();
        ticket.setId(UUID.randomUUID());
        ticket.setShortCode("VALID123");
        ticket.setShowtime(showtime);
        ticket.setTicketStatus(TicketStatus.PURCHASED);
        ticket.setPrice(BigDecimal.TEN);

        ticketDto = TicketDto.builder()
                .id(ticket.getId().toString())
                .shortcode(ticket.getShortCode())
                .price(ticket.getPrice().doubleValue())
                .build();

        request = ValidateTicketRequest.builder()
                .shortCode(ticket.getShortCode())
                .cinemaId(cinemaId)
                .build();
    }

    @Test
    void process_ValidTicket() {
        // Arrange
        when(Ticket.findByShortCode(ticket.getShortCode())).thenReturn(Optional.of(ticket));
        when(ticketMapper.ticketToTicketDto(ticket)).thenReturn(ticketDto);

        // Act
        ValidateTicketResponse response = validateTicketService.process(request);

        // Assert
        assertEquals(ticketDto, response.getTicket());
    }

    @Test
    void process_InvalidTicketCode() {
        // Arrange
        when(Ticket.findByShortCode("INVALID")).thenReturn(Optional.empty());

        ValidateTicketRequest invalidRequest = ValidateTicketRequest.builder()
                .shortCode("INVALID")
                .cinemaId(cinemaId)
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> validateTicketService.process(invalidRequest));
    }

    @Test
    void process_WrongCinema() {
        // Arrange
        UUID wrongCinemaId = UUID.randomUUID();
        request.setCinemaId(wrongCinemaId);

        when(Ticket.findByShortCode(ticket.getShortCode())).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> validateTicketService.process(request));
    }

    @Test
    void process_TicketNotPurchased() {
        // Arrange
        ticket.setTicketStatus(TicketStatus.BOOKED);

        when(Ticket.findByShortCode(ticket.getShortCode())).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThrows(InvalidResourceException.class, () -> validateTicketService.process(request));
    }

    @Test
    void process_ShowtimeEnded() {
        // Arrange
        ticket.getShowtime().setStartTime(LocalDateTime.now().minusHours(3));

        when(Ticket.findByShortCode(ticket.getShortCode())).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThrows(InvalidResourceException.class, () -> validateTicketService.process(request));
    }

    @Test
    void process_ShowtimeStartsInMoreThanAllowedTime() {
        // Arrange
        ticket.getShowtime().setStartTime(LocalDateTime.now().plusHours(2));

        when(Ticket.findByShortCode(ticket.getShortCode())).thenReturn(Optional.of(ticket));

        // Act & Assert
        assertThrows(InvalidResourceException.class, () -> validateTicketService.process(request));
    }
}
