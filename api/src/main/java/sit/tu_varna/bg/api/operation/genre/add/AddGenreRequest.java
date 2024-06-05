package sit.tu_varna.bg.api.operation.genre.add;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddGenreRequest implements ServiceRequest {
    @NotBlank
    private String name;
}
