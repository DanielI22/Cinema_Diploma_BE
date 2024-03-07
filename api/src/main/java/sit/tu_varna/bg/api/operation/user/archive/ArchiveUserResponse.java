package sit.tu_varna.bg.api.operation.user.archive;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ArchiveUserResponse implements ServiceResponse {
    private String username;
    private Boolean deleted;
}
