package sit.tu_varna.bg.api.operation.showtime.upcoming;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetUpcomingShowtimeResponse implements ServiceResponse {
    private Boolean upcoming;
}
