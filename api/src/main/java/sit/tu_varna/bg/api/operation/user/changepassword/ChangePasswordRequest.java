package sit.tu_varna.bg.api.operation.user.changepassword;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest implements ServiceRequest {
    private UUID userId;

    @NotBlank(message = "Password is required.")
    @Size(min = 3, max = 100, message = "'Password' must have a length between 3 and 100")
    private String password;
}
