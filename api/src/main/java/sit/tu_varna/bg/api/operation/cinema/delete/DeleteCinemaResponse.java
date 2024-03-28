package sit.tu_varna.bg.api.operation.cinema.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteCinemaResponse implements ServiceResponse {
    private boolean deleted;
}
