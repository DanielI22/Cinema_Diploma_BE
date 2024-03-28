package sit.tu_varna.bg.api.operation.cinema.getall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.CinemaDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllCinemasResponse implements ServiceResponse {
    private Collection<CinemaDto> cinemas;
}
