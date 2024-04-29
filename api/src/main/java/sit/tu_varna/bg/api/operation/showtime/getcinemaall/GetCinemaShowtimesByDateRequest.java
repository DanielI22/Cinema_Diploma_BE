package sit.tu_varna.bg.api.operation.showtime.getcinemaall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCinemaShowtimesByDateRequest implements ServiceRequest {
    private String cinemaId;
    private LocalDate showtimeDate;
}
