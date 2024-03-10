package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.MovieDto;
import sit.tu_varna.bg.entity.Movie;

import java.util.stream.Collectors;

@ApplicationScoped
public class MovieMapper {
    @Inject
    GenreMapper genreMapper;

    public MovieDto movieToMovieDto(Movie movie) {
        return MovieDto.builder()
                .title(movie.getTitle())
                .trailerUrl(movie.getTrailerUrl())
                .imageUrl(movie.getPosterImageUrl())
                .releaseYear(movie.getReleaseYear())
                .description(movie.getDescription())
                .description(movie.getDescription())
                .genres(movie.getGenres().
                        stream()
                        .map(g -> genreMapper.genreToGenreDto(g))
                        .collect(Collectors.toList()))
                .build();
    }
}
