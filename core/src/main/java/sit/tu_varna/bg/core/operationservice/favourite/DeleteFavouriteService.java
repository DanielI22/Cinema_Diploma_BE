package sit.tu_varna.bg.core.operationservice.favourite;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.favourite.delete.DeleteFavouriteOperation;
import sit.tu_varna.bg.api.operation.favourite.delete.DeleteFavouriteRequest;
import sit.tu_varna.bg.api.operation.favourite.delete.DeleteFavouriteResponse;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.User;

import java.util.UUID;

@ApplicationScoped
public class DeleteFavouriteService implements DeleteFavouriteOperation {

    @Transactional
    @Override
    public DeleteFavouriteResponse process(DeleteFavouriteRequest request) {
        UUID userId = request.getUserId();
        UUID movieId = request.getMovieId();

        User user = (User) User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        Movie movie = (Movie) Movie.findByIdOptional(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id " + movieId + " not found"));

        user.getFavoriteMovies().remove(movie);
        user.persist();

        return DeleteFavouriteResponse.builder().deleted(true).build();
    }
}
