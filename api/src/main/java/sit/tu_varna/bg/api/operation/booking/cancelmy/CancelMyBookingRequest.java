package sit.tu_varna.bg.api.operation.booking.cancelmy;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelMyBookingRequest implements ServiceRequest {
    private UUID bookingId;
    private UUID userId;
}
