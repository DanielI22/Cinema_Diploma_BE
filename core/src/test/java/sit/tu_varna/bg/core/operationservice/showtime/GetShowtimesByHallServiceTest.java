package sit.tu_varna.bg.core.operationservice.showtime;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.ShowtimeDto;
import sit.tu_varna.bg.api.operation.showtime.gethallbydate.GetShowtimesByHallRequest;
import sit.tu_varna.bg.api.operation.showtime.gethallbydate.GetShowtimesByHallResponse;
import sit.tu_varna.bg.core.mapper.ShowtimeMapper;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Showtime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
class GetShowtimesByHallServiceTest {

    @InjectMocks
    GetShowtimesByHallService getShowtimesByHallService;

    @Mock
    ShowtimeMapper showtimeMapper;

    private UUID hallId;
    private LocalDate showtimeDate;
    private Showtime showtime1, showtime2;
    private ShowtimeDto showtimeDto1, showtimeDto2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(Showtime.class);

        hallId = UUID.randomUUID();
        showtimeDate = LocalDate.now();

        Hall hall = new Hall();
        hall.setId(hallId);

        showtime1 = new Showtime();
        showtime1.setId(UUID.randomUUID());
        showtime1.setStartTime(LocalDateTime.now().plusHours(1));
        showtime1.setHall(hall);

        showtime2 = new Showtime();
        showtime2.setId(UUID.randomUUID());
        showtime2.setStartTime(LocalDateTime.now().plusHours(2));
        showtime2.setHall(hall);

        showtimeDto1 = ShowtimeDto.builder()
                .id(showtime1.getId().toString())
                .startTime(showtime1.getStartTime())
                .build();

        showtimeDto2 = ShowtimeDto.builder()
                .id(showtime2.getId().toString())
                .startTime(showtime2.getStartTime())
                .build();
    }

    @Test
    void process_SuccessfulResponse() {
        // Arrange
        when(Showtime.findByDate(showtimeDate)).thenReturn(List.of(showtime1, showtime2));
        when(showtimeMapper.showtimeToShowtimeDto(showtime1)).thenReturn(showtimeDto1);
        when(showtimeMapper.showtimeToShowtimeDto(showtime2)).thenReturn(showtimeDto2);

        GetShowtimesByHallRequest request = GetShowtimesByHallRequest.builder()
                .hallId(hallId)
                .showtimeDate(showtimeDate)
                .build();

        // Act
        GetShowtimesByHallResponse response = getShowtimesByHallService.process(request);

        // Assert
        assertEquals(2, response.getShowtimes().size());
        assertEquals(showtimeDto1, response.getShowtimes().stream().findFirst().orElse(null));
        assertEquals(showtimeDto2, response.getShowtimes().stream().skip(1).findFirst().orElse(null));
    }
}
