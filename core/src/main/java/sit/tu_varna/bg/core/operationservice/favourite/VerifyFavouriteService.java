package sit.tu_varna.bg.core.operationservice.favourite;

import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.favourite.verify.VerifyFavouriteOperation;
import sit.tu_varna.bg.api.operation.favourite.verify.VerifyFavouriteRequest;
import sit.tu_varna.bg.api.operation.favourite.verify.VerifyFavouriteResponse;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.User;

import java.util.UUID;

@ApplicationScoped
public class VerifyFavouriteService implements VerifyFavouriteOperation {

    @Override
    public VerifyFavouriteResponse process(VerifyFavouriteRequest request) {
        UUID userId = request.getUserId();
        UUID movieId = request.getMovieId();

        User user = (User) User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
        Movie movie = (Movie) Movie.findByIdOptional(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId));
        Boolean isFavourite = user.getFavoriteMovies().contains(movie);

        return VerifyFavouriteResponse.builder().isFavourite(isFavourite).build();
    }
}
