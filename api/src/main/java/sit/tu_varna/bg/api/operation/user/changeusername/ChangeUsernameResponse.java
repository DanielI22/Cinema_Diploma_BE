package sit.tu_varna.bg.api.operation.user.changeusername;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeUsernameResponse implements ServiceResponse {
    private String userId;
}
