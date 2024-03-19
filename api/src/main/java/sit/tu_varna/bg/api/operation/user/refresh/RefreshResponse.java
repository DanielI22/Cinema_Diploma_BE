package sit.tu_varna.bg.api.operation.user.refresh;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshResponse implements ServiceResponse {
    private String accessToken;
    private String refreshToken;
}
