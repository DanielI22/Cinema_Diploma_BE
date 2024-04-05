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
    @NotBlank(message = "Title is required.")
    private String title;
    private String description;
    private String imageUrl;
    private String trailerUrl;
    @NotNull(message = "Release year is required.")
    private Integer releaseYear;
    @NotNull(message = "Duration is required.")
    private Integer duration;
    private Collection<GenreDto> genres;
}
