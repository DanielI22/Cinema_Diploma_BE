package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.api.dto.CinemaDto;
import sit.tu_varna.bg.core.interfaces.ObjectMapper;
import sit.tu_varna.bg.entity.Cinema;

@ApplicationScoped
public class CinemaMapper implements ObjectMapper {
    public CinemaDto cinemaToCinemaDto(Cinema cinema) {
        return CinemaDto.builder()
                .id(cinema.getId().toString())
                .name(cinema.getName())
                .location(cinema.getLocation())
                .imageUrl(cinema.getImageUrl())
                .build();
    }
}
