package sit.tu_varna.bg.api.dto;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieDto {
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private String trailerUrl;
    private Integer releaseYear;
    private Integer duration;
    private Collection<GenreDto> genres;
}
