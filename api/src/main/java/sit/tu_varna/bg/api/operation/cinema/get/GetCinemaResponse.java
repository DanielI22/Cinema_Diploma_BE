package sit.tu_varna.bg.api.operation.cinema.get;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.CinemaDto;
import sit.tu_varna.bg.api.dto.CinemaHallDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCinemaResponse implements ServiceResponse {
    private CinemaDto cinema;
    private Collection<CinemaHallDto> halls;
}
