package sit.tu_varna.bg.api.operation.ticket.get;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetTicketRequest implements ServiceRequest {
    private UUID ticketId;
}
