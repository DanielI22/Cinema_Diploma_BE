package sit.tu_varna.bg.api.operation.showtime.getmovieall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.ShowtimeDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMovieShowtimesByDateResponse implements ServiceResponse {
    private Collection<ShowtimeDto> showtimes;
}
