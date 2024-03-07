package sit.tu_varna.bg.core.converter.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import sit.tu_varna.bg.api.dto.GenreDto;
import sit.tu_varna.bg.api.dto.MovieDto;
import sit.tu_varna.bg.data.entity.Genre;
import sit.tu_varna.bg.data.entity.Movie;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MovieToMovieDto implements Converter<Movie, MovieDto> {
    private final Converter<Genre, GenreDto> genreConverter;

    @Override
    public MovieDto convert(Movie source) {
        return MovieDto.builder()
                .title(source.getTitle())
                .description(source.getDescription())
                .releaseYear(source.getReleaseYear())
                .imageUrl(source.getPosterImageUrl())
                .trailerUrl(source.getTrailerUrl())
                .genres(source.getGenres()
                        .stream()
                        .map(genreConverter::convert)
                        .collect(Collectors.toList()))
                .build();
    }
}
