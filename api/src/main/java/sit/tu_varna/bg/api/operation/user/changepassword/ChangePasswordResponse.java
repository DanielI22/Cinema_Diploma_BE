package sit.tu_varna.bg.api.operation.user.changepassword;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordResponse implements ServiceResponse {
    private String userId;
}
