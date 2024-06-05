package sit.tu_varna.bg.core.operationservice.favourite;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.favourite.add.AddFavouriteOperation;
import sit.tu_varna.bg.api.operation.favourite.add.AddFavouriteRequest;
import sit.tu_varna.bg.api.operation.favourite.add.AddFavouriteResponse;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.User;

import java.util.UUID;

@ApplicationScoped
public class AddFavouriteService implements AddFavouriteOperation {

    @Transactional
    @Override
    public AddFavouriteResponse process(AddFavouriteRequest request) {
        UUID userId = request.getUserId();
        UUID movieId = request.getMovieId();

        User user = (User) User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        Movie movie = (Movie) Movie.findByIdOptional(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id " + movieId + " not found"));

        user.getFavoriteMovies().add(movie);
        user.persist();

        return AddFavouriteResponse.builder().added(true).build();
    }
}
