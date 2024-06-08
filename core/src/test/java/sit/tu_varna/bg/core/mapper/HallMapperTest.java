package sit.tu_varna.bg.core.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.HallDto;
import sit.tu_varna.bg.api.dto.RowDto;
import sit.tu_varna.bg.api.dto.SeatDto;
import sit.tu_varna.bg.entity.*;

import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HallMapperTest {

    @InjectMocks
    HallMapper hallMapper;

    Hall hall;
    Row row;
    Seat seat;
    ShowtimeSeat showtimeSeat;
    UUID showtimeId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID hallId = UUID.randomUUID();
        UUID rowId = UUID.randomUUID();
        UUID seatId = UUID.randomUUID();
        showtimeId = UUID.randomUUID();

        Cinema cinema = Cinema.builder()
                .id(UUID.randomUUID())
                .name("Test Cinema")
                .build();

        hall = Hall.builder()
                .id(hallId)
                .name("Hall 1")
                .cinema(cinema)
                .build();

        row = Row.builder()
                .id(rowId)
                .rowNumber(1)
                .hall(hall)
                .build();

        seat = Seat.builder()
                .id(seatId)
                .seatNumber(1)
                .row(row)
                .isEmptySpace(false)
                .build();

        showtimeSeat = ShowtimeSeat.builder()
                .id(UUID.randomUUID())
                .seat(seat)
                .showtime(Showtime.builder().id(showtimeId).build())
                .isBooked(true)
                .build();

        row.setSeats(Collections.singletonList(seat));
        hall.setRows(Collections.singletonList(row));
    }

    @Test
    void testHallToHallDto() {
        HallDto hallDto = hallMapper.hallToHallDto(hall);

        assertEquals(hall.getId().toString(), hallDto.getId());
        assertEquals(hall.getName(), hallDto.getName());
        assertEquals(hall.getCinema().getName(), hallDto.getCinemaName());
    }

    @Test
    void testRowToRowDto() {
        RowDto rowDto = hallMapper.rowToRowDto(row);

        assertEquals(row.getId().toString(), rowDto.getId());
        assertEquals(row.getRowNumber(), rowDto.getRowNumber());
        assertEquals(1, rowDto.getSeats().size());
        assertEquals(seat.getSeatNumber(), Objects.requireNonNull(rowDto.getSeats().stream().findFirst().orElse(null)).getSeatNumber());
    }

    @Test
    void testSeatToSeatDto() {
        SeatDto seatDto = hallMapper.seatToSeatDto(seat);

        assertEquals(seat.getId().toString(), seatDto.getId());
        assertEquals(seat.getSeatNumber(), seatDto.getSeatNumber());
        assertEquals(seat.getRow().getRowNumber(), seatDto.getRowNumber());
        assertEquals(seat.isEmptySpace(), seatDto.getIsEmpty());
    }
}
