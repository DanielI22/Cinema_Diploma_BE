package sit.tu_varna.bg.api.operation.cinema.gethalls;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.HallDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCinemaHallsResponse implements ServiceResponse {
    private Collection<HallDto> halls;
}
