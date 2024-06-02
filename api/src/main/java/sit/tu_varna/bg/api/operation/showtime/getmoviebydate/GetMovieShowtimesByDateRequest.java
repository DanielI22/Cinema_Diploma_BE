package sit.tu_varna.bg.api.operation.showtime.getmoviebydate;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMovieShowtimesByDateRequest implements ServiceRequest {
    private UUID movieId;
    private LocalDate showtimeDate;
}
