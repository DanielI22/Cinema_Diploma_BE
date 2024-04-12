package sit.tu_varna.bg.api.operation.genre.edit;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditGenreResponse implements ServiceResponse {
    private String genreId;
}
