package sit.tu_varna.bg.api.operation.booking.cancelmy;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelMyBookingResponse implements ServiceResponse {
    private Boolean cancelled;
}
