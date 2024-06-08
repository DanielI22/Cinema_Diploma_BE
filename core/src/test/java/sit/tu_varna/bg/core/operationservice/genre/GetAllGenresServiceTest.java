package sit.tu_varna.bg.core.operationservice.genre;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.GenreDto;
import sit.tu_varna.bg.api.operation.genre.getall.GetAllGenresRequest;
import sit.tu_varna.bg.api.operation.genre.getall.GetAllGenresResponse;
import sit.tu_varna.bg.core.mapper.GenreMapper;
import sit.tu_varna.bg.entity.Genre;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
class GetAllGenresServiceTest {

    @Inject
    GetAllGenresService getAllGenresService;

    @Mock
    GenreMapper genreMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(Genre.class);
    }

    @Test
    void process_ReturnsAllGenres() {
        // Arrange
        UUID genreId1 = UUID.randomUUID();
        UUID genreId2 = UUID.randomUUID();

        Genre genre1 = mock(Genre.class);
        when(genre1.getId()).thenReturn(genreId1);
        when(genre1.getName()).thenReturn("Action");
        when(genre1.getCreatedOn()).thenReturn(Instant.now());

        Genre genre2 = mock(Genre.class);
        when(genre2.getId()).thenReturn(genreId2);
        when(genre2.getName()).thenReturn("Drama");
        when(genre2.getCreatedOn()).thenReturn(Instant.now().minusSeconds(3600));

        when(Genre.listAll()).thenReturn(List.of(genre1, genre2));

        GenreDto genreDto1 = GenreDto.builder().id(genreId1.toString()).name("Action").build();
        GenreDto genreDto2 = GenreDto.builder().id(genreId2.toString()).name("Drama").build();

        when(genreMapper.genreToGenreDto(genre1)).thenReturn(genreDto1);
        when(genreMapper.genreToGenreDto(genre2)).thenReturn(genreDto2);

        GetAllGenresRequest request = new GetAllGenresRequest();

        // Act
        GetAllGenresResponse response = getAllGenresService.process(request);

        // Assert
        List<GenreDto> expectedGenres = Arrays.asList(genreDto1, genreDto2);
        assertEquals(expectedGenres.stream().map(GenreDto::getName).collect(Collectors.toList()),
                response.getGenres().stream().map(GenreDto::getName).collect(Collectors.toList()));
    }
}
