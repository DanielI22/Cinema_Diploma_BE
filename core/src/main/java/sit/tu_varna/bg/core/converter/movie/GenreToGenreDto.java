package sit.tu_varna.bg.core.converter.movie;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sit.tu_varna.bg.api.dto.GenreDto;
import sit.tu_varna.bg.data.entity.Genre;

@Component
public class GenreToGenreDto implements Converter<Genre, GenreDto> {
    @Override
    public GenreDto convert(Genre source) {
        return GenreDto.builder()
                .name(source.getName())
                .build();
    }
}
