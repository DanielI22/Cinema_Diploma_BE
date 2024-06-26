package sit.tu_varna.bg.api.operation.hall.edit;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;
import sit.tu_varna.bg.api.dto.RowDto;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditHallRequest implements ServiceRequest {
    private UUID hallId;
    @NotBlank
    private String name;
    private Collection<RowDto> rows;
}
