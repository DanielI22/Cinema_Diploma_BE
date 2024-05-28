package sit.tu_varna.bg.core.operationservice.ticket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.TicketDto;
import sit.tu_varna.bg.api.operation.ticket.getmytickets.GetMyTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.getmytickets.GetMyTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.getmytickets.GetMyTicketsResponse;
import sit.tu_varna.bg.core.mapper.TicketMapper;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.enums.TicketStatus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetMyTicketsService implements GetMyTicketsOperation {
    @Inject
    TicketMapper ticketMapper;

    @Override
    public GetMyTicketsResponse process(GetMyTicketsRequest request) {
        List<TicketDto> tickets = Ticket.findByUserId(request.getUserId())
                .stream()
                .filter(ticket -> ticket.getTicketStatus().equals(TicketStatus.PURCHASED))
                .sorted(Comparator.comparing(Ticket::getCreatedOn).reversed())
                .map(ticketMapper::ticketToTicketDto)
                .collect(Collectors.toList());

        return GetMyTicketsResponse.builder()
                .tickets(tickets)
                .build();
    }
}
