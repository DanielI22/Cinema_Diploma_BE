package sit.tu_varna.bg.api.operation.genre.edit;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditGenreRequest implements ServiceRequest {
    private UUID genreId;
    @NotBlank(message = "Name is required.")
    private String name;
}
