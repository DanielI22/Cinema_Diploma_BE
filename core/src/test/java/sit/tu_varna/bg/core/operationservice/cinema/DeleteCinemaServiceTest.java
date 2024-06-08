package sit.tu_varna.bg.core.operationservice.cinema;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.cinema.delete.DeleteCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.delete.DeleteCinemaResponse;
import sit.tu_varna.bg.entity.Cinema;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class DeleteCinemaServiceTest {

    DeleteCinemaService deleteCinemaService;

    UUID cinemaId;
    Cinema mockCinema;

    @BeforeEach
    void setUp() {
        PanacheMock.mock(Cinema.class);
        deleteCinemaService = new DeleteCinemaService();

        cinemaId = UUID.randomUUID();
        mockCinema = mock(Cinema.class);
        when(mockCinema.getId()).thenReturn(cinemaId);
        when(mockCinema.isPersistent()).thenReturn(true);
    }

    @Test
    void process_SuccessfulDeletion() {
        // Arrange
        DeleteCinemaRequest request = new DeleteCinemaRequest();
        request.setCinemaId(cinemaId);

        when(Cinema.findByIdOptional(cinemaId)).thenReturn(Optional.of(mockCinema));

        // Act
        DeleteCinemaResponse response = deleteCinemaService.process(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getDeleted());
        verify(mockCinema, times(1)).delete();
    }

    @Test
    void process_CinemaNotFound() {
        // Arrange
        UUID nonExistentId = UUID.randomUUID();
        DeleteCinemaRequest request = new DeleteCinemaRequest();
        request.setCinemaId(nonExistentId);

        when(Cinema.findByIdOptional(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> deleteCinemaService.process(request));
        verify(mockCinema, never()).delete();
    }

    @Test
    void process_CinemaNotPersistent() {
        // Arrange
        DeleteCinemaRequest request = new DeleteCinemaRequest();
        request.setCinemaId(cinemaId);

        when(Cinema.findByIdOptional(cinemaId)).thenReturn(Optional.of(mockCinema));
        when(mockCinema.isPersistent()).thenReturn(false);

        // Act
        DeleteCinemaResponse response = deleteCinemaService.process(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getDeleted());
        verify(mockCinema, never()).delete();
    }
}
