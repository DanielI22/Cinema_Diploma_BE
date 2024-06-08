package sit.tu_varna.bg.core.operationservice.movie;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.movie.delete.DeleteMovieRequest;
import sit.tu_varna.bg.api.operation.movie.delete.DeleteMovieResponse;
import sit.tu_varna.bg.entity.Movie;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
public class DeleteMovieServiceTest {

    @Inject
    DeleteMovieService deleteMovieService;

    UUID movieId;
    Movie movie;

    @BeforeEach
    void setUp() {
        PanacheMock.mock(Movie.class);

        movieId = UUID.randomUUID();
        movie = mock(Movie.class);
        when(movie.getId()).thenReturn(movieId);
        movie.setId(movieId);
    }

    @Test
    void process_SuccessfulDeletion() {
        // Arrange
        when(Movie.findByIdOptional(movieId)).thenReturn(Optional.of(movie));
        when(movie.isPersistent()).thenReturn(true);

        DeleteMovieRequest request = new DeleteMovieRequest();
        request.setMovieId(movieId);

        // Act
        DeleteMovieResponse response = deleteMovieService.process(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getDeleted());
        verify(movie, times(1)).delete();
    }

    @Test
    void process_MovieNotFound() {
        // Arrange
        when(Movie.findByIdOptional(movieId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () ->
                deleteMovieService.process(DeleteMovieRequest.builder().movieId(movieId).build()));
    }
}
