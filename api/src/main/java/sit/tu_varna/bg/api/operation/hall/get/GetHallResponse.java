package sit.tu_varna.bg.api.operation.hall.get;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.CinemaHallDto;
import sit.tu_varna.bg.api.dto.RowDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetHallResponse implements ServiceResponse {
    private CinemaHallDto hall;
    private Collection<RowDto> rows;
}
