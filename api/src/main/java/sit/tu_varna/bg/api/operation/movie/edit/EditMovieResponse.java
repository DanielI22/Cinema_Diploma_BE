package sit.tu_varna.bg.api.operation.movie.edit;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditMovieResponse implements ServiceResponse {
    private String movieId;
}
