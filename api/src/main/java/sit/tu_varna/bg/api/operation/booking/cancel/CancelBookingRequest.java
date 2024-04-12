package sit.tu_varna.bg.api.operation.booking.cancel;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelBookingRequest implements ServiceRequest {
    private UUID bookingId;
}
