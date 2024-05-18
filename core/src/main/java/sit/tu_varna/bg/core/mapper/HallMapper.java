package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.api.dto.HallDto;
import sit.tu_varna.bg.api.dto.RowDto;
import sit.tu_varna.bg.api.dto.SeatDto;
import sit.tu_varna.bg.core.interfaces.ObjectMapper;
import sit.tu_varna.bg.entity.*;

import java.util.Comparator;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class HallMapper implements ObjectMapper {
    public HallDto hallToHallDto(Hall hall) {
        String cinemaName = Optional.ofNullable(hall.getCinema()).map(Cinema::getName).orElse(null);
        return HallDto.builder()
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

    public SeatDto seatToShowtimeSeatDto(Seat seat, UUID showtimeId) {
        Optional<ShowtimeSeat> showtimeSeat = ShowtimeSeat.findBySeatIdAndShowtimeId(seat.getId(), showtimeId).stream().findFirst();
        return SeatDto.builder()
                .id(seat.getId().toString())
                .seatNumber(seat.getSeatNumber())
                .isEmpty(seat.isEmptySpace())
                .isBooked(showtimeSeat.isPresent() && showtimeSeat.get().isBooked())
                .build();
    }

    public RowDto rowToShowtimeRowDto(Row row, UUID showtimeId) {
        return RowDto.builder()
                .id(row.getId().toString())
                .rowNumber(row.getRowNumber())
                .seats(row.getSeats().stream()
                        .sorted(Comparator.comparingInt(Seat::getSeatNumber))
                        .map(seat -> seatToShowtimeSeatDto(seat, showtimeId))
                        .collect(Collectors.toList()))
                .build();
    }
}
