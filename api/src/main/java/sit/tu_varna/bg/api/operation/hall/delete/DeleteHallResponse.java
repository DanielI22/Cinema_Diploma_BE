package sit.tu_varna.bg.api.operation.hall.delete;

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
