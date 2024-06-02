package sit.tu_varna.bg.api.operation.showtime.current;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetCurrentShowtimeResponse implements ServiceResponse {
    private Boolean current;
}
