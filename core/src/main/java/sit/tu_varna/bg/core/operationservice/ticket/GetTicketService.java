package sit.tu_varna.bg.core.operationservice.ticket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.ticket.get.GetTicketOperation;
import sit.tu_varna.bg.api.operation.ticket.get.GetTicketRequest;
import sit.tu_varna.bg.api.operation.ticket.get.GetTicketResponse;
import sit.tu_varna.bg.core.mapper.TicketMapper;
import sit.tu_varna.bg.entity.Ticket;

import java.util.UUID;

@ApplicationScoped
public class GetTicketService implements GetTicketOperation {
    @Inject
    TicketMapper ticketMapper;

    @Override
    public GetTicketResponse process(GetTicketRequest request) {
        UUID ticketId = request.getTicketId();
        Ticket ticket = (Ticket) Ticket.findByIdOptional(ticketId)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket with id " + ticketId + " not found"));

        return GetTicketResponse.builder()
                .ticket(ticketMapper.ticketToTicketDto(ticket))
                .build();
    }
}
