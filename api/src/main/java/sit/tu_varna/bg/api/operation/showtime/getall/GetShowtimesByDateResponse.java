package sit.tu_varna.bg.api.operation.showtime.getall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.ShowtimeDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShowtimesByDateResponse implements ServiceResponse {
    private Collection<ShowtimeDto> showtimes;
}
