package sit.tu_varna.bg.core.operationservice.cinema;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.HallDto;
import sit.tu_varna.bg.api.operation.cinema.gethalls.GetCinemaHallsRequest;
import sit.tu_varna.bg.api.operation.cinema.gethalls.GetCinemaHallsResponse;
import sit.tu_varna.bg.core.mapper.HallMapper;
import sit.tu_varna.bg.entity.Hall;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
class GetCinemaHallsServiceTest {

    @Inject
    GetCinemaHallsService getCinemaHallsService;

    @Mock
    HallMapper hallMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(Hall.class);
    }

    @Test
    void process_ReturnsCinemaHalls() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();

        Hall hall1 = mock(Hall.class);
        when(hall1.getId()).thenReturn(UUID.randomUUID());
        when(hall1.getName()).thenReturn("Hall 1");

        Hall hall2 = mock(Hall.class);
        when(hall2.getId()).thenReturn(UUID.randomUUID());
        when(hall2.getName()).thenReturn("Hall 2");

        List<Hall> hallList = Arrays.asList(hall1, hall2);

        when(Hall.findByCinemaId(cinemaId)).thenReturn(hallList);

        HallDto hallDto1 = HallDto.builder().id(hall1.getId().toString()).name("Hall 1").build();
        HallDto hallDto2 = HallDto.builder().id(hall2.getId().toString()).name("Hall 2").build();

        when(hallMapper.hallToHallDto(hall1)).thenReturn(hallDto1);
        when(hallMapper.hallToHallDto(hall2)).thenReturn(hallDto2);

        GetCinemaHallsRequest request = GetCinemaHallsRequest.builder()
                .cinemaId(cinemaId)
                .build();

        // Act
        GetCinemaHallsResponse response = getCinemaHallsService.process(request);

        // Assert
        List<HallDto> expectedHalls = Arrays.asList(hallDto1, hallDto2);
        assertEquals(expectedHalls.stream().map(HallDto::getName).collect(Collectors.toList()),
                response.getHalls().stream().map(HallDto::getName).collect(Collectors.toList()));
    }

    @Test
    void process_NoHalls_ReturnsEmptyList() {
        // Arrange
        UUID cinemaId = UUID.randomUUID();

        when(Hall.findByCinemaId(cinemaId)).thenReturn(Collections.emptyList());

        GetCinemaHallsRequest request = GetCinemaHallsRequest.builder()
                .cinemaId(cinemaId)
                .build();

        // Act
        GetCinemaHallsResponse response = getCinemaHallsService.process(request);

        // Assert
        assertEquals(Collections.emptyList(), response.getHalls());
    }
}
