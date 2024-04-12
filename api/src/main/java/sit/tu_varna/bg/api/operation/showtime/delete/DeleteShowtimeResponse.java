package sit.tu_varna.bg.api.operation.showtime.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteShowtimeResponse implements ServiceResponse {
    private Boolean deleted;
}
