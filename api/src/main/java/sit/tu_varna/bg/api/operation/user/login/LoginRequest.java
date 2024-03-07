package sit.tu_varna.bg.api.operation.user.login;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest implements ServiceRequest {
    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "Password is required.")
    private String password;
}
