package sit.tu_varna.bg.api.operation.movie.externalapi;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMoviesImdbTagResult {
    private List<MovieSearchResult> movie_results;
}
