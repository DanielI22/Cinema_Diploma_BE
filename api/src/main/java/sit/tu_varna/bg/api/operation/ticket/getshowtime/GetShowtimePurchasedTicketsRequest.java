package sit.tu_varna.bg.api.operation.ticket.getshowtime;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShowtimePurchasedTicketsRequest implements ServiceRequest {
    private UUID showtimeId;
}
