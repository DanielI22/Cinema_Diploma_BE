package sit.tu_varna.bg.api.operation.booking.take;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TakeBookingRequest implements ServiceRequest {
    @NotNull
    private UUID bookingId;
    private UUID userId;
}
