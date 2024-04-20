package sit.tu_varna.bg.api.operation.showtime.getmovieall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMovieShowtimesByDateRequest implements ServiceRequest {
    private String movieId;
    private LocalDate showtimeDate;
}
