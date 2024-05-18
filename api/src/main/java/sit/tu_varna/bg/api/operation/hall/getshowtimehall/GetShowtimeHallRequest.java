package sit.tu_varna.bg.api.operation.hall.getshowtimehall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShowtimeHallRequest implements ServiceRequest {
    private UUID showtimeId;
}
