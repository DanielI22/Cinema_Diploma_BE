package sit.tu_varna.bg.api.operation.hall.getall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.HallDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllHallsResponse implements ServiceResponse {
    private Collection<HallDto> halls;
}
