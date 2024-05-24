package sit.tu_varna.bg.api.operation.booking.getshowtime;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShowtimeBookingsRequest implements ServiceRequest {
    private UUID showtimeId;
}
