package sit.tu_varna.bg.core.operationservice.genre;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.genre.delete.DeleteGenreRequest;
import sit.tu_varna.bg.api.operation.genre.delete.DeleteGenreResponse;
import sit.tu_varna.bg.entity.Genre;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class DeleteGenreServiceTest {

    DeleteGenreService deleteGenreService;

    UUID genreId;
    Genre mockGenre;

    @BeforeEach
    void setUp() {
        PanacheMock.mock(Genre.class);
        deleteGenreService = new DeleteGenreService();

        genreId = UUID.randomUUID();
        mockGenre = mock(Genre.class);
        when(mockGenre.getId()).thenReturn(genreId);
    }

    @Test
    void process_GenreFound() {
        // Arrange
        when(Genre.findByIdOptional(genreId)).thenReturn(Optional.of(mockGenre));
        when(mockGenre.isPersistent()).thenReturn(true);

        DeleteGenreRequest request = new DeleteGenreRequest();
        request.setGenreId(genreId);

        // Act
        DeleteGenreResponse response = deleteGenreService.process(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getDeleted());
        verify(mockGenre, times(1)).delete();
    }

    @Test
    void process_GenreNotFound() {
        // Arrange
        when(Genre.findByIdOptional(genreId)).thenReturn(Optional.empty());

        DeleteGenreRequest request = new DeleteGenreRequest();
        request.setGenreId(genreId);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> deleteGenreService.process(request));
    }

    @Test
    void process_GenreNotPersistent() {
        // Arrange
        when(Genre.findByIdOptional(genreId)).thenReturn(Optional.of(mockGenre));
        when(mockGenre.isPersistent()).thenReturn(false);

        DeleteGenreRequest request = new DeleteGenreRequest();
        request.setGenreId(genreId);

        // Act
        DeleteGenreResponse response = deleteGenreService.process(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getDeleted());
        verify(mockGenre, never()).delete();
    }
}
