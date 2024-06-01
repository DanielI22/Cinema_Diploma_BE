package sit.tu_varna.bg.api.operation.ticket.validate;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.TicketDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateTicketResponse implements ServiceResponse {
    private TicketDto ticket;
}
