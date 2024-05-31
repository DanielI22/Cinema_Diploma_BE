package sit.tu_varna.bg.api.operation.booking.take;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TakeBookingResponse implements ServiceResponse {
    private Collection<UUID> ticketIds;
}
