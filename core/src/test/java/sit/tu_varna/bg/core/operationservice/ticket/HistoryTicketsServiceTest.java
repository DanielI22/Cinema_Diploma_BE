package sit.tu_varna.bg.core.operationservice.ticket;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.OperatorTicketDto;
import sit.tu_varna.bg.api.operation.ticket.history.HistoryTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.history.HistoryTicketsResponse;
import sit.tu_varna.bg.core.mapper.TicketMapper;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.entity.User;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
class HistoryTicketsServiceTest {

    @InjectMocks
    HistoryTicketsService historyTicketsService;

    @Mock
    TicketMapper ticketMapper;

    private UUID userId;
    private UUID cinemaId;
    private Ticket ticket1, ticket2;
    private OperatorTicketDto operatorTicketDto1, operatorTicketDto2;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the Panache entities
        PanacheMock.mock(Ticket.class);

        userId = UUID.randomUUID();
        cinemaId = UUID.randomUUID();

        User user = new User();
        user.setId(userId);

        Cinema cinema = new Cinema();
        cinema.setId(cinemaId);

        ticket1 = new Ticket();
        ticket1.setId(UUID.randomUUID());
        ticket1.setShortCode("ABC123");
        ticket1.setShowtimeSeat(null);
        ticket1.setTicketStatus(null);
        ticket1.setUser(user);
        ticket1.setShowtime(null);
        ticket1.setCreatedOn(LocalDateTime.now().minusDays(1).toInstant(ZoneOffset.UTC));
        ticket1.setPrice(null);

        ticket2 = new Ticket();
        ticket2.setId(UUID.randomUUID());
        ticket2.setShortCode("XYZ456");
        ticket2.setShowtimeSeat(null);
        ticket2.setTicketStatus(null);
        ticket2.setUser(user);
        ticket2.setShowtime(null);
        ticket2.setCreatedOn(LocalDateTime.now().minusDays(2).toInstant(ZoneOffset.UTC));
        ticket2.setPrice(null);

        operatorTicketDto1 = OperatorTicketDto.builder()
                .id(ticket1.getId().toString())
                .shortcode(ticket1.getShortCode())
                .soldTime(LocalDateTime.ofInstant(ticket1.getCreatedOn(), ZoneId.systemDefault()))
                .build();

        operatorTicketDto2 = OperatorTicketDto.builder()
                .id(ticket2.getId().toString())
                .shortcode(ticket2.getShortCode())
                .soldTime(LocalDateTime.ofInstant(ticket2.getCreatedOn(), ZoneId.systemDefault()))
                .build();
    }

    @Test
    void process_SuccessfulResponse() {
        // Arrange
        int pageNumber = 0;
        int pageSize = 2;

        when(Ticket.findLastSoldTickets(userId, cinemaId, pageNumber, pageSize))
                .thenReturn(List.of(ticket1, ticket2));
        when(ticketMapper.ticketToOperatorTicketDto(ticket1)).thenReturn(operatorTicketDto1);
        when(ticketMapper.ticketToOperatorTicketDto(ticket2)).thenReturn(operatorTicketDto2);
        when(Ticket.countTickets(userId, cinemaId)).thenReturn(4L);

        HistoryTicketsRequest request = HistoryTicketsRequest.builder()
                .userId(userId)
                .cinemaId(cinemaId)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .build();

        // Act
        HistoryTicketsResponse response = historyTicketsService.process(request);

        // Assert
        assertEquals(2, response.getTickets().size());
        assertEquals(operatorTicketDto1, response.getTickets().stream().findFirst().orElse(null));
        assertEquals(operatorTicketDto2, response.getTickets().stream().skip(1).findFirst().orElse(null));
        assertEquals(2, response.getTotalPages());
    }
}
