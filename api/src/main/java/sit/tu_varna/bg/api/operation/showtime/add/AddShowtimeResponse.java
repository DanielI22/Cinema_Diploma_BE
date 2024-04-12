package sit.tu_varna.bg.api.operation.showtime.add;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddShowtimeResponse implements ServiceResponse {
    private String showtimeId;
}
