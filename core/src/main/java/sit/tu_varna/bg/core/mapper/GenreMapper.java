package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.api.dto.GenreDto;
import sit.tu_varna.bg.api.operation.movie.externalapi.MovieApiResult;
import sit.tu_varna.bg.entity.Genre;

@ApplicationScoped
public class GenreMapper {
    public GenreDto genreToGenreDto(Genre genre) {
        return GenreDto.builder()
                .id(genre.getId().toString())
                .name(genre.getName())
                .build();
    }

    public GenreDto genreApiToGenreDto(MovieApiResult.GenreApiResult genreApiResult) {
        return GenreDto.builder()
                .name(genreApiResult.getName())
                .build();
    }
}
