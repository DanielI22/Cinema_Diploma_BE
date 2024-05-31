package sit.tu_varna.bg.api.operation.ticket.history;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.OperatorTicketDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryTicketsResponse implements ServiceResponse {
    private Collection<OperatorTicketDto> tickets;
    private int totalPages;
}
