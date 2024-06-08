package sit.tu_varna.bg.core.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.GenreDto;
import sit.tu_varna.bg.api.dto.MovieDto;
import sit.tu_varna.bg.api.operation.movie.externalapi.MovieApiResult;
import sit.tu_varna.bg.entity.Genre;
import sit.tu_varna.bg.entity.Movie;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class MovieMapperTest {

    @InjectMocks
    MovieMapper movieMapper;

    @Mock
    GenreMapper genreMapper;

    Movie movie;
    MovieApiResult movieApiResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UUID genreId1 = UUID.randomUUID();
        UUID genreId2 = UUID.randomUUID();
        UUID movieId = UUID.randomUUID();
        UUID apiGenreId1 = UUID.randomUUID();
        UUID apiGenreId2 = UUID.randomUUID();

        Genre genre1 = Genre.builder()
                .id(genreId1)
                .name("Action")
                .build();

        Genre genre2 = Genre.builder()
                .id(genreId2)
                .name("Drama")
                .build();

        movie = Movie.builder()
                .id(movieId)
                .title("Sample Movie")
                .trailerUrl("https://sampleurl.com/trailer")
                .posterImageUrl("https://sampleurl.com/image.jpg")
                .releaseYear(2021)
                .description("Sample Description")
                .duration(120)
                .genres(Set.of(genre1, genre2))
                .build();

        MovieApiResult.GenreApiResult genreApiResult1 = MovieApiResult.GenreApiResult.builder()
                .name("Action")
                .build();

        MovieApiResult.GenreApiResult genreApiResult2 = MovieApiResult.GenreApiResult.builder()
                .name("Drama")
                .build();

        MovieApiResult.VideoSingleApiResult videoApiResult = MovieApiResult.VideoSingleApiResult.builder()
                .key("sample_key")
                .build();

        movieApiResult = MovieApiResult.builder()
                .id(5)
                .title("Sample Movie API")
                .poster_path("/sample_image.jpg")
                .overview("Sample API Description")
                .release_date(LocalDate.of(2021, 12, 15))
                .runtime(130)
                .genres(List.of(genreApiResult1, genreApiResult2))
                .videos(MovieApiResult.VideoApiResult.builder()
                        .results(List.of(videoApiResult))
                        .build())
                .build();

        when(genreMapper.genreToGenreDto(genre1)).thenReturn(GenreDto.builder().id(genreId1.toString()).name("Action").build());
        when(genreMapper.genreToGenreDto(genre2)).thenReturn(GenreDto.builder().id(genreId2.toString()).name("Drama").build());
        when(genreMapper.genreApiToGenreDto(genreApiResult1)).thenReturn(GenreDto.builder().id(apiGenreId1.toString()).name("Action").build());
        when(genreMapper.genreApiToGenreDto(genreApiResult2)).thenReturn(GenreDto.builder().id(apiGenreId2.toString()).name("Drama").build());
    }

    @Test
    void testMovieToMovieDto() {
        MovieDto movieDto = movieMapper.movieToMovieDto(movie);

        assertEquals(movie.getId().toString(), movieDto.getId());
        assertEquals(movie.getTitle(), movieDto.getTitle());
        assertEquals(movie.getTrailerUrl(), movieDto.getTrailerUrl());
        assertEquals(movie.getPosterImageUrl(), movieDto.getImageUrl());
        assertEquals(movie.getReleaseYear(), movieDto.getReleaseYear());
        assertEquals(movie.getDescription(), movieDto.getDescription());
        assertEquals(movie.getDuration(), movieDto.getDuration());
        assertEquals(movie.getGenres().size(), movieDto.getGenres().size());
    }

    @Test
    void testMovieApiToMovieDto() {
        MovieDto movieDto = movieMapper.movieApiToMovieDto(movieApiResult);

        assertEquals(movieApiResult.getId().toString(), movieDto.getId());
        assertEquals(movieApiResult.getTitle(), movieDto.getTitle());
        assertEquals("https://image.tmdb.org/t/p/original/" + movieApiResult.getPoster_path(), movieDto.getImageUrl());
        assertEquals(movieApiResult.getRelease_date().getYear(), movieDto.getReleaseYear());
        assertEquals(movieApiResult.getOverview(), movieDto.getDescription());
        assertEquals(movieApiResult.getRuntime(), movieDto.getDuration());
        assertEquals(movieApiResult.getGenres().size(), movieDto.getGenres().size());
        assertEquals(movieApiResult.getVideos().getResults().get(0).getKey(), movieDto.getTrailerUrl());
    }
}
