package sit.tu_varna.bg.api.operation.user.add;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddUserResponse implements ServiceResponse {
    private String userId;
}
