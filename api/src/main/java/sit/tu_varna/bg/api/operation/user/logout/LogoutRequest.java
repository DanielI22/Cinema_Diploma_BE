package sit.tu_varna.bg.api.operation.user.logout;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoutRequest implements ServiceRequest {
    private String userId;
}
