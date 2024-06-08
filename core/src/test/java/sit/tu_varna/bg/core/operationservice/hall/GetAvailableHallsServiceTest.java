package sit.tu_varna.bg.core.operationservice.hall;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.HallDto;
import sit.tu_varna.bg.api.operation.hall.getavailable.GetAvailableHallsRequest;
import sit.tu_varna.bg.api.operation.hall.getavailable.GetAvailableHallsResponse;
import sit.tu_varna.bg.core.mapper.HallMapper;
import sit.tu_varna.bg.entity.Hall;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
class GetAvailableHallsServiceTest {

    @InjectMocks
    GetAvailableHallsService getAvailableHallsService;

    @InjectMock
    HallMapper hallMapper;

    @Mock
    Hall mockHall;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(Hall.class);
    }

    @Test
    void process_AvailableHallsFound() {
        // Arrange
        HallDto mockHallDto = mock(HallDto.class);
        when(mockHall.getId()).thenReturn(UUID.randomUUID());
        when(Hall.findAvailable()).thenReturn(List.of(mockHall));
        when(hallMapper.hallToHallDto(mockHall)).thenReturn(mockHallDto);

        // Act
        GetAvailableHallsResponse response = getAvailableHallsService.process(new GetAvailableHallsRequest());

        // Assert
        assertEquals(1, response.getHalls().size());
        assertEquals(mockHallDto, response.getHalls().stream().findFirst().orElse(null));
    }

    @Test
    void process_NoAvailableHalls() {
        // Arrange
        when(Hall.findAvailable()).thenReturn(Collections.emptyList());

        // Act
        GetAvailableHallsResponse response = getAvailableHallsService.process(new GetAvailableHallsRequest());

        // Assert
        assertEquals(0, response.getHalls().size());
    }
}
