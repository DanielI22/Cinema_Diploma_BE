package sit.tu_varna.bg.api.operation.hall.deletehall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteHallResponse implements ServiceResponse {
    private Boolean deleted;
}
