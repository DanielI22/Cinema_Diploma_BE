package sit.tu_varna.bg.core.operationservice.ticket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.OperatorTicketDto;
import sit.tu_varna.bg.api.operation.ticket.history.HistoryTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.history.HistoryTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.history.HistoryTicketsResponse;
import sit.tu_varna.bg.core.mapper.TicketMapper;
import sit.tu_varna.bg.entity.Ticket;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class HistoryTicketsService implements HistoryTicketsOperation {
    @Inject
    TicketMapper ticketMapper;

    @Override
    public HistoryTicketsResponse process(HistoryTicketsRequest request) {
        UUID userId = request.getUserId();
        UUID cinemaId = request.getCinemaId();
        int pageSize = request.getPageSize();
        List<OperatorTicketDto> tickets = Ticket.findLastSoldTickets(userId, cinemaId,
                        request.getPageNumber(), pageSize)
                .stream()
                .map(ticketMapper::ticketToOperatorTicketDto)
                .collect(Collectors.toList());

        int totalPages = (int) Math.ceil((double) Ticket.countTickets(userId, cinemaId) / pageSize);
        return HistoryTicketsResponse.builder()
                .tickets(tickets)
                .totalPages(totalPages)
                .build();
    }
}
