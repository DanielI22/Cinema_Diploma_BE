package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.MovieDto;
import sit.tu_varna.bg.api.operation.movie.externalapi.MovieApiResult;
import sit.tu_varna.bg.core.interfaces.ObjectMapper;
import sit.tu_varna.bg.entity.Genre;
import sit.tu_varna.bg.entity.Movie;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class MovieMapper implements ObjectMapper {
    @Inject
    GenreMapper genreMapper;

    public MovieDto movieToMovieDto(Movie movie) {
        return MovieDto.builder()
                .id(movie.getId().toString())
                .title(movie.getTitle())
                .trailerUrl(movie.getTrailerUrl())
                .imageUrl(movie.getPosterImageUrl())
                .releaseYear(movie.getReleaseYear())
                .description(movie.getDescription())
                .duration(movie.getDuration())
                .genres(movie.getGenres().
                        stream()
                        .sorted(Comparator.comparing(Genre::getName))
                        .map(g -> genreMapper.genreToGenreDto(g))
                        .collect(Collectors.toList()))
                .build();
    }

    public MovieDto movieApiToMovieDto(MovieApiResult movieApiResult) {
        String tmdbUri = "https://image.tmdb.org/t/p/original/";
        return MovieDto.builder()
                .id(movieApiResult.getId().toString())
                .title(movieApiResult.getTitle())
                .trailerUrl(movieApiResult.getVideos().getResults()
                        .stream().findFirst().map(MovieApiResult.VideoSingleApiResult::getKey)
                        .orElse(null))
                .imageUrl(tmdbUri + movieApiResult.getPoster_path())
                .releaseYear(Optional.ofNullable(movieApiResult.getRelease_date())
                        .map(LocalDate::getYear).orElse(null))
                .description(movieApiResult.getOverview())
                .duration(movieApiResult.getRuntime())
                .genres(movieApiResult.getGenres().
                        stream()
                        .sorted(Comparator.comparing(MovieApiResult.GenreApiResult::getName))
                        .map(g -> genreMapper.genreApiToGenreDto(g))
                        .collect(Collectors.toList()))
                .build();
    }
}
