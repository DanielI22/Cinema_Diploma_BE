package sit.tu_varna.bg.api.operation.hall.get;

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
public class GetHallResponse implements ServiceResponse {
    private HallDto hall;
    private Collection<RowDto> rows;
}
