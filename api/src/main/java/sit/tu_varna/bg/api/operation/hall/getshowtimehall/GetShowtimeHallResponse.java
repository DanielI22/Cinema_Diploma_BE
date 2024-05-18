package sit.tu_varna.bg.api.operation.hall.getshowtimehall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.HallDto;
import sit.tu_varna.bg.api.dto.RowDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShowtimeHallResponse implements ServiceResponse {
    private HallDto hall;
    private Collection<RowDto> rows;
}
