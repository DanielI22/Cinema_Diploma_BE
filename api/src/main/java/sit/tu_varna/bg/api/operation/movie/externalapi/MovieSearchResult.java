package sit.tu_varna.bg.api.operation.movie.externalapi;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieSearchResult {
    private String id;
    private String title;
}
