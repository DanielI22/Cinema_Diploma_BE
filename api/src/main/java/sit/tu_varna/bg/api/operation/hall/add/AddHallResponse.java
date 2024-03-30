package sit.tu_varna.bg.api.operation.hall.add;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddHallResponse implements ServiceResponse {
    private String hallId;
}
