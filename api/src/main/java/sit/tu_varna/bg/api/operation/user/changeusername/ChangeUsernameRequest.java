package sit.tu_varna.bg.api.operation.user.changeusername;

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
public class ChangeUsernameRequest implements ServiceRequest {
    private UUID userId;

    @NotBlank
    @Size(min = 3, max = 255, message = "'Username' must have a length between 3 and 255")
    private String username;
}
