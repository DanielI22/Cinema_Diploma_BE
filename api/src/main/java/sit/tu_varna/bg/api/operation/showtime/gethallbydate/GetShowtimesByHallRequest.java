package sit.tu_varna.bg.api.operation.showtime.gethallbydate;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShowtimesByHallRequest implements ServiceRequest {
    private UUID hallId;
    private LocalDate showtimeDate;
}
