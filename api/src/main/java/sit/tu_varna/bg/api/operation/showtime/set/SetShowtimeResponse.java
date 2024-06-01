package sit.tu_varna.bg.api.operation.showtime.set;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SetShowtimeResponse implements ServiceResponse {
    private Boolean set;
}
