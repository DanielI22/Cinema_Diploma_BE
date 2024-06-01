package sit.tu_varna.bg.api.operation.showtime.end;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndShowtimeResponse implements ServiceResponse {
    private Boolean ended;
}
