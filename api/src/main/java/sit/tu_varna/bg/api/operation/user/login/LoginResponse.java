package sit.tu_varna.bg.api.operation.user.login;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse implements ServiceResponse {
    private String accessToken;
    private String refreshToken;
}
