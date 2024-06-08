package sit.tu_varna.bg.core.operationservice.showtime;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.ShowtimeDto;
import sit.tu_varna.bg.api.operation.showtime.getcinemabydate.GetCinemaShowtimesByDateRequest;
import sit.tu_varna.bg.api.operation.showtime.getcinemabydate.GetCinemaShowtimesByDateResponse;
import sit.tu_varna.bg.core.mapper.ShowtimeMapper;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Showtime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
class GetCinemaShowtimesByDateServiceTest {

    @InjectMocks
    GetCinemaShowtimesByDateService getCinemaShowtimesByDateService;

    @Mock
    ShowtimeMapper showtimeMapper;

    private UUID cinemaId;
    private LocalDate showtimeDate;
    private Showtime showtime1, showtime2;
    private ShowtimeDto showtimeDto1, showtimeDto2;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Mock the Panache entities
        PanacheMock.mock(Showtime.class);

        cinemaId = UUID.randomUUID();
        showtimeDate = LocalDate.now();

        Cinema cinema = new Cinema();
        cinema.setId(cinemaId);

        showtime1 = new Showtime();
        showtime1.setId(UUID.randomUUID());
        showtime1.setStartTime(LocalDateTime.now().plusHours(1));
        showtime1.setCinema(cinema);

        showtime2 = new Showtime();
        showtime2.setId(UUID.randomUUID());
        showtime2.setStartTime(LocalDateTime.now().plusHours(2));
        showtime2.setCinema(cinema);

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

        GetCinemaShowtimesByDateRequest request = GetCinemaShowtimesByDateRequest.builder()
                .cinemaId(cinemaId)
                .showtimeDate(showtimeDate)
                .build();

        // Act
        GetCinemaShowtimesByDateResponse response = getCinemaShowtimesByDateService.process(request);

        // Assert
        assertEquals(2, response.getShowtimes().size());
        assertEquals(showtimeDto1, response.getShowtimes().stream().findFirst().orElse(null));
        assertEquals(showtimeDto2, response.getShowtimes().stream().skip(1).findFirst().orElse(null));
    }
}
