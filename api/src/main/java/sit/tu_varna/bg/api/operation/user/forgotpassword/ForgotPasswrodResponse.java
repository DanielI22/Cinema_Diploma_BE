package sit.tu_varna.bg.api.operation.user.forgotpassword;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ForgotPasswrodResponse implements ServiceResponse {
    private boolean sent;
}
