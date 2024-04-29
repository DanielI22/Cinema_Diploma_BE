package sit.tu_varna.bg.api.operation.showtime.getcinemaall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.ShowtimeDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCinemaShowtimesByDateResponse implements ServiceResponse {
    private Collection<ShowtimeDto> showtimes;
}
