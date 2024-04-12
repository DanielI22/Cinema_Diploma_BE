package sit.tu_varna.bg.api.operation.cinema.getHalls;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.CinemaHallDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCinemaHallsResponse implements ServiceResponse {
    private Collection<CinemaHallDto> halls;
}
