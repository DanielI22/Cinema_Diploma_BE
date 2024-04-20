package sit.tu_varna.bg.core.operationservice.movie;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.MovieDto;
import sit.tu_varna.bg.api.operation.movie.getbygenre.GetMoviesByGenreOperation;
import sit.tu_varna.bg.api.operation.movie.getbygenre.GetMoviesByGenreResponse;
import sit.tu_varna.bg.api.operation.movie.getbygenre.GetMoviesByGenreRequest;
import sit.tu_varna.bg.core.mapper.MovieMapper;
import sit.tu_varna.bg.entity.Movie;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetMoviesByGenreService implements GetMoviesByGenreOperation {
    @Inject
    MovieMapper movieMapper;

    @Override
    public GetMoviesByGenreResponse process(GetMoviesByGenreRequest request) {
        List<MovieDto> movies = Movie.findByGenreName(request.getGenreName())
                .stream()
                .map(m -> movieMapper.movieToMovieDto(m))
                .collect(Collectors.toList());
        return GetMoviesByGenreResponse.builder()
                .movies(movies)
                .build();
    }
}
