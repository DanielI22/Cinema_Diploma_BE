package sit.tu_varna.bg.api.operation.user.password;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeResponse implements ServiceResponse {
    private String username;
    private String newPassword;
}
