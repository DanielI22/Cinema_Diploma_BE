package sit.tu_varna.bg.api.operation.user.register;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse implements ServiceResponse {
    private String userId;
    private String username;
    private String role;
}
