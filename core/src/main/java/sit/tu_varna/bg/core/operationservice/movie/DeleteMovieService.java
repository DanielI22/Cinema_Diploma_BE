package sit.tu_varna.bg.core.operationservice.movie;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.movie.delete.DeleteMovieOperation;
import sit.tu_varna.bg.api.operation.movie.delete.DeleteMovieRequest;
import sit.tu_varna.bg.api.operation.movie.delete.DeleteMovieResponse;
import sit.tu_varna.bg.entity.Movie;

import java.util.UUID;

@ApplicationScoped
public class DeleteMovieService implements DeleteMovieOperation {

    @Transactional
    @Override
    public DeleteMovieResponse process(DeleteMovieRequest request) {
        UUID movieId = request.getMovieId();
        Movie movie = (Movie) Movie.findByIdOptional(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id " + movieId + " not found"));
        if (movie.isPersistent()) {
            movie.delete();
        }
        return DeleteMovieResponse.builder().deleted(true).build();
    }
}
