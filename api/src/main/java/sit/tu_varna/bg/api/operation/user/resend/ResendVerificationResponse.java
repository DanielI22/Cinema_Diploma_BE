package sit.tu_varna.bg.api.operation.user.resend;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResendVerificationResponse implements ServiceResponse {
    private boolean sent;
}
