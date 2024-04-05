package sit.tu_varna.bg.api.operation.movie.add;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddMovieResponse implements ServiceResponse {
    private String movieId;
}
