package sit.tu_varna.bg.api.operation.showtime.getcinemabydate;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCinemaShowtimesByDateRequest implements ServiceRequest {
    private UUID cinemaId;
    private LocalDate showtimeDate;
}
