package sit.tu_varna.bg.core.operationservice.movie;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.MovieDto;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesOperation;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesRequest;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesResponse;
import sit.tu_varna.bg.core.mapper.MovieMapper;
import sit.tu_varna.bg.entity.Movie;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetAllMoviesService implements GetAllMoviesOperation {
    @Inject
    MovieMapper movieMapper;

    @Override
    public GetAllMoviesResponse process(GetAllMoviesRequest request) {
        List<MovieDto> movies = Movie.listAll()
                .stream()
                .filter(Movie.class::isInstance)
                .map(Movie.class::cast)
                .sorted(Comparator.comparing(Movie::getCreatedOn).reversed())
                .map(m -> movieMapper.movieToMovieDto(m))
                .collect(Collectors.toList());
        return GetAllMoviesResponse.builder()
                .movies(movies)
                .build();
    }
}
