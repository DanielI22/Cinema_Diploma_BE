package sit.tu_varna.bg.api.operation.movie.externalapi;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.MovieDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMoviesResponse implements ServiceResponse {
    private Collection<MovieDto> movies;
}
