package sit.tu_varna.bg.api.operation.user.password;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordChangeRequest implements ServiceRequest {
    @NotBlank(message = "New password is required.")
    private String newPassword;
}
