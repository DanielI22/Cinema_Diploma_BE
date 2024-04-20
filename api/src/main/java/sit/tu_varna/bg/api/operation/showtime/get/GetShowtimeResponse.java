package sit.tu_varna.bg.api.operation.showtime.get;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.ShowtimeDto;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShowtimeResponse implements ServiceResponse {
    private ShowtimeDto showtime;
    private UUID cinemaId;
    private UUID hallId;
    private UUID movieId;
}
