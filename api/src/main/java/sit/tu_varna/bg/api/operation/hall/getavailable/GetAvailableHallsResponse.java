package sit.tu_varna.bg.api.operation.hall.getavailable;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.CinemaHallDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAvailableHallsResponse implements ServiceResponse {
    private Collection<CinemaHallDto> halls;
}
