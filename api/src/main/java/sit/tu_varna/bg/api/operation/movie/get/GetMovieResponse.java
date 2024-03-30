package sit.tu_varna.bg.api.operation.movie.get;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.MovieDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMovieResponse implements ServiceResponse {
    private MovieDto movie;
}
