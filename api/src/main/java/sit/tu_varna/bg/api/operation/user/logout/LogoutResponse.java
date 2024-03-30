package sit.tu_varna.bg.api.operation.user.logout;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutResponse implements ServiceResponse {
    private Boolean success;
}
