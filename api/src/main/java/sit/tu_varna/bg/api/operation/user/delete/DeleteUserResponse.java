package sit.tu_varna.bg.api.operation.user.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserResponse implements ServiceResponse {
    private Boolean deleted;
}
