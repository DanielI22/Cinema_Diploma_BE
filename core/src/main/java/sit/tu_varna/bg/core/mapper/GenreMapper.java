package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.api.dto.GenreDto;
import sit.tu_varna.bg.entity.Genre;

@ApplicationScoped
public class GenreMapper {
    public GenreDto genreToGenreDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId().toString())
                .name(genre.getName())
                .build();
    }
}
