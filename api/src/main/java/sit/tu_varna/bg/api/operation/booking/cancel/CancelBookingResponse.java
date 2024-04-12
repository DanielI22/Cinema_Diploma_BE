package sit.tu_varna.bg.api.operation.booking.cancel;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelBookingResponse implements ServiceResponse {
    private Boolean cancelled;
}
