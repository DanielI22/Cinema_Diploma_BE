package sit.tu_varna.bg.api.operation.movie.getbygenre;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMoviesByGenreRequest implements ServiceRequest {
    @NotBlank(message = "Genre cannot be empty!")
    private String genreName;
}
