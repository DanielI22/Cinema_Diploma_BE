package sit.tu_varna.bg.api.operation.ticket.purchase;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddTicketsResponse implements ServiceResponse {
    private Collection<String> ticketIds;
}
