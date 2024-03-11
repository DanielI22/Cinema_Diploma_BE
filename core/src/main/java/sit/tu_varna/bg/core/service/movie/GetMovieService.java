package sit.tu_varna.bg.core.service.movie;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.movie.getbyid.GetMovieOperation;
import sit.tu_varna.bg.api.operation.movie.getbyid.GetMovieRequest;
import sit.tu_varna.bg.api.operation.movie.getbyid.GetMovieResponse;
import sit.tu_varna.bg.core.mapper.MovieMapper;
import sit.tu_varna.bg.entity.Movie;

import java.util.UUID;

@ApplicationScoped
public class GetMovieService implements GetMovieOperation {
    @Inject
    MovieMapper movieMapper;

    @Override
    public GetMovieResponse process(GetMovieRequest request) {
        UUID movieId = request.getMovieId();
        Movie movie = (Movie) Movie.findByIdOptional(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id " + movieId + " not found"));

        return GetMovieResponse.builder()
                .movie(movieMapper.movieToMovieDto(movie))
                .build();
    }
}
