package sit.tu_varna.bg.api.operation.movie.add;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;
import sit.tu_varna.bg.api.dto.GenreDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddMovieRequest implements ServiceRequest {
    @NotBlank
    private String title;
    private String description;
    private String imageUrl;
    private String trailerUrl;
    @NotNull
    private Integer releaseYear;
    @NotNull
    private Integer duration;
    private Collection<GenreDto> genres;
}
