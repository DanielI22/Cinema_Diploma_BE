package sit.tu_varna.bg.api.operation.movie.edit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditMovieRequest implements ServiceRequest {
    private UUID movieId;
    @NotBlank
    private String title;
    private String description;
    private String imageUrl;
    private String trailerUrl;
    @NotNull
    private Integer releaseYear;
    @NotNull
    private Integer duration;
    private Collection<UUID> genres;
}
