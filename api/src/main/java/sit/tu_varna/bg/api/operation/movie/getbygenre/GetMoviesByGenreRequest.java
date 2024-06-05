package sit.tu_varna.bg.api.operation.movie.getbygenre;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMoviesByGenreRequest implements ServiceRequest {
    private String genreName;
}
