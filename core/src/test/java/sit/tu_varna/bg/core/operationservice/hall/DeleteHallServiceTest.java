package sit.tu_varna.bg.core.operationservice.hall;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.hall.delete.DeleteHallRequest;
import sit.tu_varna.bg.api.operation.hall.delete.DeleteHallResponse;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;
import sit.tu_varna.bg.entity.Seat;
import sit.tu_varna.bg.entity.ShowtimeSeat;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
class DeleteHallServiceTest {

    DeleteHallService deleteHallService;

    @BeforeEach
    void setUp() {
        deleteHallService = new DeleteHallService();
        PanacheMock.mock(Hall.class);
        PanacheMock.mock(Row.class);
        PanacheMock.mock(Seat.class);
        PanacheMock.mock(ShowtimeSeat.class);
    }

    @Test
    void process_SuccessfulDeletion() {
        // Arrange
        UUID hallId = UUID.randomUUID();
        Hall mockHall = mock(Hall.class);
        Row mockRow = mock(Row.class);
        Seat mockSeat = mock(Seat.class);
        ShowtimeSeat mockShowtimeSeat = mock(ShowtimeSeat.class);

        when(Hall.findByIdOptional(hallId)).thenReturn(Optional.of(mockHall));
        when(mockHall.getRows()).thenReturn(List.of(mockRow));
        when(mockRow.getSeats()).thenReturn(List.of(mockSeat));
        when(ShowtimeSeat.findBySeatId(mockSeat.getId())).thenReturn(List.of(mockShowtimeSeat));
        when(mockHall.isPersistent()).thenReturn(true);

        // Act
        DeleteHallResponse response = deleteHallService.process(DeleteHallRequest.builder().hallId(hallId).build());

        // Assert
        assertEquals(true, response.getDeleted());
        verify(mockHall, times(1)).delete();
        verify(mockRow, times(1)).delete();
        verify(mockSeat, times(1)).delete();
        verify(mockShowtimeSeat, times(1)).delete();
    }

    @Test
    void process_HallNotFound() {
        // Arrange
        UUID hallId = UUID.randomUUID();
        when(Hall.findByIdOptional(hallId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> deleteHallService.process(DeleteHallRequest.builder().hallId(hallId).build()));
    }
}
