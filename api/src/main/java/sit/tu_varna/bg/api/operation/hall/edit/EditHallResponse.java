package sit.tu_varna.bg.api.operation.hall.edit;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditHallResponse implements ServiceResponse {
    private String hallId;
}
