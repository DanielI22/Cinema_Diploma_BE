package sit.tu_varna.bg.api.operation.showtime.getbydate;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShowtimesByDateRequest implements ServiceRequest {
    private LocalDate showtimeDate;
}
