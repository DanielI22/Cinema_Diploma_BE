package sit.tu_varna.bg.api.operation.user.register;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest implements ServiceRequest {
    @NotBlank
    @Size(min = 3, max = 255, message = "'Username' must have a length between 3 and 255")
    private String username;

    @Email(message = "Invalid email format.", regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 3, max = 100, message = "'Password' must have a length between 3 and 100")
    private String password;
}
