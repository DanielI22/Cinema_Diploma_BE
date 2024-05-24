package sit.tu_varna.bg.api.operation.user.getall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.UserDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetUsersResponse implements ServiceResponse {
    private Collection<UserDto> users;
}
