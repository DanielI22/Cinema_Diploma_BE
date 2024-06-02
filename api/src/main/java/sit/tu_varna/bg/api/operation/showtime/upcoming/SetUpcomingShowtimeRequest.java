package sit.tu_varna.bg.api.operation.showtime.upcoming;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetUpcomingShowtimeRequest implements ServiceRequest {
    private UUID showtimeId;
}
