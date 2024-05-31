package sit.tu_varna.bg.api.operation.ticket.get;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.TicketDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTicketResponse implements ServiceResponse {
    private TicketDto ticket;
}
