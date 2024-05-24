package sit.tu_varna.bg.api.operation.ticket.getshowtime;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.TicketDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShowtimePurchasedTicketsResponse implements ServiceResponse {
    private Collection<TicketDto> tickets;
}
