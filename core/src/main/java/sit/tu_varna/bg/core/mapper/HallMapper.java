package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.api.dto.CinemaHallDto;
import sit.tu_varna.bg.api.dto.HallCinemaDto;
import sit.tu_varna.bg.api.dto.RowDto;
import sit.tu_varna.bg.api.dto.SeatDto;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;
import sit.tu_varna.bg.entity.Seat;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class HallMapper {
    public CinemaHallDto hallToCinemaHallDto(Hall hall) {
        return CinemaHallDto.builder()
                .id(hall.getId().toString())
                .name(hall.getName())
                .build();
    }

    public HallCinemaDto hallToHallCinemaDto(Hall hall) {
        String cinemaName = Optional.ofNullable(hall.getCinema()).map(Cinema::getName).orElse(null);
        return HallCinemaDto.builder()
                .id(hall.getId().toString())
                .name(hall.getName())
                .cinemaName(cinemaName)
                .build();
    }

    public SeatDto seatToSeatDto(Seat seat) {
        return SeatDto.builder()
                .id(seat.getId().toString())
                .seatNumber(seat.getSeatNumber())
                .isEmpty(seat.isEmptySpace())
                .build();
    }

    public RowDto rowToRowDto(Row row) {
        return RowDto.builder()
                .id(row.getId().toString())
                .rowNumber(row.getRowNumber())
                .seats(row.getSeats().stream()
                        .sorted(Comparator.comparingInt(Seat::getSeatNumber))
                        .map(this::seatToSeatDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
