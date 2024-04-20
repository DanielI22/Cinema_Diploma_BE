package sit.tu_varna.bg.api.operation.showtime.edit;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditShowtimeResponse implements ServiceResponse {
    private String showtimeId;
}
