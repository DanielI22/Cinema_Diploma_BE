package sit.tu_varna.bg.api.operation.ticket.getmy;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMyTicketsRequest implements ServiceRequest {
    private UUID userId;
}
