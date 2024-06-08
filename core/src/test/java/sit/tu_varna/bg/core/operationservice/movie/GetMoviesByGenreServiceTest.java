package sit.tu_varna.bg.core.operationservice.movie;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.MovieDto;
import sit.tu_varna.bg.api.operation.movie.getbygenre.GetMoviesByGenreRequest;
import sit.tu_varna.bg.api.operation.movie.getbygenre.GetMoviesByGenreResponse;
import sit.tu_varna.bg.core.mapper.MovieMapper;
import sit.tu_varna.bg.entity.Movie;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@QuarkusTest
class GetMoviesByGenreServiceTest {

    @InjectMocks
    GetMoviesByGenreService getMoviesByGenreService;

    @Mock
    MovieMapper movieMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(Movie.class);
    }

    @Test
    void process_SuccessfulRetrieval() {
        // Arrange
        String genreName = "Action";
        Movie mockMovie = new Movie();
        mockMovie.setId(UUID.randomUUID());
        mockMovie.setTitle("Action Movie");
        MovieDto mockMovieDto = new MovieDto();
        mockMovieDto.setId("1");
        mockMovieDto.setTitle("Action Movie");

        when(Movie.findByGenreName(genreName)).thenReturn(List.of(mockMovie));
        when(movieMapper.movieToMovieDto(mockMovie)).thenReturn(mockMovieDto);

        GetMoviesByGenreRequest request = GetMoviesByGenreRequest.builder()
                .genreName(genreName)
                .build();

        // Act
        GetMoviesByGenreResponse response = getMoviesByGenreService.process(request);

        // Assert
        assertEquals(1, response.getMovies().size());
        assertEquals("1", Objects.requireNonNull(response.getMovies().stream().findFirst().orElse(null)).getId());
        assertEquals("Action Movie", Objects.requireNonNull(response.getMovies().stream().findFirst().orElse(null)).getTitle());
    }
}
