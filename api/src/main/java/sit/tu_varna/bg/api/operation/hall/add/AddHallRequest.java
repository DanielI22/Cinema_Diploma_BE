package sit.tu_varna.bg.api.operation.hall.add;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;
import sit.tu_varna.bg.api.dto.RowDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddHallRequest implements ServiceRequest {
    @NotBlank(message = "Name is required.")
    private String name;
    private Collection<RowDto> rows;
}
