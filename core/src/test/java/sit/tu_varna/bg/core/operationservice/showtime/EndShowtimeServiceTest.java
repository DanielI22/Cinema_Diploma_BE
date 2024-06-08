package sit.tu_varna.bg.core.operationservice.showtime;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.end.EndShowtimeRequest;
import sit.tu_varna.bg.entity.Showtime;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@QuarkusTest
class EndShowtimeServiceTest {

    EndShowtimeService endShowtimeService;

    @BeforeEach
    void setUp() {
        PanacheMock.mock(Showtime.class);
        endShowtimeService = new EndShowtimeService();
    }

    @Test
    void process_ShowtimeNotFound() {
        // Arrange
        UUID showtimeId = UUID.randomUUID();
        when(Showtime.findByIdOptional(showtimeId)).thenReturn(Optional.empty());

        EndShowtimeRequest request = new EndShowtimeRequest();
        request.setShowtimeId(showtimeId);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> endShowtimeService.process(request));
    }
}
