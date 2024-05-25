package sit.tu_varna.bg.api.operation.booking.getmybookings;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMyBookingsRequest implements ServiceRequest {
    private UUID userId;
}
