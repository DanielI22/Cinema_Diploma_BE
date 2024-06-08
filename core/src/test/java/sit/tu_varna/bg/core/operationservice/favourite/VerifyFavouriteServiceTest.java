package sit.tu_varna.bg.core.operationservice.favourite;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.favourite.verify.VerifyFavouriteRequest;
import sit.tu_varna.bg.api.operation.favourite.verify.VerifyFavouriteResponse;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.User;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@QuarkusTest
class VerifyFavouriteServiceTest {

    VerifyFavouriteService verifyFavouriteService;

    UUID userId;
    UUID movieId;
    User mockUser;
    Movie mockMovie;

    @BeforeEach
    void setUp() {
        PanacheMock.mock(User.class);
        PanacheMock.mock(Movie.class);
        verifyFavouriteService = new VerifyFavouriteService();

        userId = UUID.randomUUID();
        movieId = UUID.randomUUID();

        mockUser = Mockito.mock(User.class);
        mockMovie = Mockito.mock(Movie.class);

        when(mockUser.getId()).thenReturn(userId);
        when(mockMovie.getId()).thenReturn(movieId);
    }

    @Test
    void process_UserAndMovieFound_Favourite() {
        // Arrange
        HashSet<Movie> favouriteMovies = new HashSet<>();
        favouriteMovies.add(mockMovie);
        when(mockUser.getFavoriteMovies()).thenReturn(favouriteMovies);

        when(User.findByIdOptional(userId)).thenReturn(Optional.of(mockUser));
        when(Movie.findByIdOptional(movieId)).thenReturn(Optional.of(mockMovie));

        VerifyFavouriteRequest request = new VerifyFavouriteRequest();
        request.setUserId(userId);
        request.setMovieId(movieId);

        // Act
        VerifyFavouriteResponse response = verifyFavouriteService.process(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getIsFavourite());
    }

    @Test
    void process_UserAndMovieFound_NotFavourite() {
        // Arrange
        HashSet<Movie> favouriteMovies = new HashSet<>();
        when(mockUser.getFavoriteMovies()).thenReturn(favouriteMovies);

        when(User.findByIdOptional(userId)).thenReturn(Optional.of(mockUser));
        when(Movie.findByIdOptional(movieId)).thenReturn(Optional.of(mockMovie));

        VerifyFavouriteRequest request = new VerifyFavouriteRequest();
        request.setUserId(userId);
        request.setMovieId(movieId);

        // Act
        VerifyFavouriteResponse response = verifyFavouriteService.process(request);

        // Assert
        assertNotNull(response);
        assertFalse(response.getIsFavourite());
    }

    @Test
    void process_UserNotFound() {
        // Arrange
        when(User.findByIdOptional(userId)).thenReturn(Optional.empty());

        VerifyFavouriteRequest request = new VerifyFavouriteRequest();
        request.setUserId(userId);
        request.setMovieId(movieId);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> verifyFavouriteService.process(request));
    }

    @Test
    void process_MovieNotFound() {
        // Arrange
        when(User.findByIdOptional(userId)).thenReturn(Optional.of(mockUser));
        when(Movie.findByIdOptional(movieId)).thenReturn(Optional.empty());

        VerifyFavouriteRequest request = new VerifyFavouriteRequest();
        request.setUserId(userId);
        request.setMovieId(movieId);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> verifyFavouriteService.process(request));
    }
}
