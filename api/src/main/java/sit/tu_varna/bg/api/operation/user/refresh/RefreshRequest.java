package sit.tu_varna.bg.api.operation.user.refresh;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefreshRequest implements ServiceRequest {
    @NotBlank(message = "Refresh token is required.")
    private String refreshToken;
}
