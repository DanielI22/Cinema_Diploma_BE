package sit.tu_varna.bg.core.operationservice.ticket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.ShowtimeTicketDto;
import sit.tu_varna.bg.api.operation.ticket.getshowtime.GetShowtimePurchasedTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.getshowtime.GetShowtimePurchasedTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.getshowtime.GetShowtimePurchasedTicketsResponse;
import sit.tu_varna.bg.core.mapper.TicketMapper;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.enums.TicketStatus;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetShowtimePurchasedTicketsService implements GetShowtimePurchasedTicketsOperation {
    @Inject
    TicketMapper ticketMapper;

    @Override
    public GetShowtimePurchasedTicketsResponse process(GetShowtimePurchasedTicketsRequest request) {
        List<ShowtimeTicketDto> tickets = Ticket.findByShowtimeId(request.getShowtimeId())
                .stream()
                .filter(ticket -> ticket.getTicketStatus().equals(TicketStatus.PURCHASED))
                .sorted(Comparator
                        .comparing((Ticket t) -> t.getShowtimeSeat().getSeat().getRow().getRowNumber())
                        .thenComparing(t -> t.getShowtimeSeat().getSeat().getSeatNumber()))
                .map(t -> ticketMapper.ticketToShowtimeTicketDto(t))
                .collect(Collectors.toList());

        return GetShowtimePurchasedTicketsResponse.builder()
                .tickets(tickets)
                .build();
    }
}
