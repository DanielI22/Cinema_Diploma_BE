package sit.tu_varna.bg.api.operation.user.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteUserRequest implements ServiceRequest {
    private UUID userId;
}
