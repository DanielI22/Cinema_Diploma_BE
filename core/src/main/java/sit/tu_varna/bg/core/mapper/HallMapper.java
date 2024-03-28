package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.api.dto.CinemaHallDto;
import sit.tu_varna.bg.api.dto.HallCinemaDto;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Hall;

import java.util.Optional;

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
}
