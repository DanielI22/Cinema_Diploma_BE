package sit.tu_varna.bg.core.operationservice.favourite;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.MovieDto;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.favourite.getall.GetFavouritesOperation;
import sit.tu_varna.bg.api.operation.favourite.getall.GetFavouritesRequest;
import sit.tu_varna.bg.api.operation.favourite.getall.GetFavouritesResponse;
import sit.tu_varna.bg.core.mapper.MovieMapper;
import sit.tu_varna.bg.entity.User;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetFavouritesService implements GetFavouritesOperation {
    @Inject
    MovieMapper movieMapper;

    @Override
    public GetFavouritesResponse process(GetFavouritesRequest request) {
        UUID userId = request.getUserId();
        User user = (User) User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        Collection<MovieDto> favourites = user.getFavoriteMovies()
                .stream()
                .map(movieMapper::movieToMovieDto)
                .collect(Collectors.toList());

        return GetFavouritesResponse.builder().movies(favourites).build();
    }
}
