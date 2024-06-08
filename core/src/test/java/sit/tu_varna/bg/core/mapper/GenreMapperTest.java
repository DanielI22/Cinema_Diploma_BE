package sit.tu_varna.bg.core.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.GenreDto;
import sit.tu_varna.bg.api.operation.movie.externalapi.MovieApiResult;
import sit.tu_varna.bg.entity.Genre;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GenreMapperTest {

    @InjectMocks
    GenreMapper genreMapper;

    private Genre genre;
    private MovieApiResult.GenreApiResult genreApiResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        genre = new Genre();
        genre.setId(UUID.randomUUID());
        genre.setName("Drama");

        genreApiResult = new MovieApiResult.GenreApiResult();
        genreApiResult.setName("Comedy");
    }

    @Test
    void testGenreToGenreDto() {
        GenreDto genreDto = genreMapper.genreToGenreDto(genre);

        assertEquals(genre.getId().toString(), genreDto.getId());
        assertEquals(genre.getName(), genreDto.getName());
    }

    @Test
    void testGenreApiToGenreDto() {
        GenreDto genreDto = genreMapper.genreApiToGenreDto(genreApiResult);

        assertEquals(genreApiResult.getName(), genreDto.getName());
        assertNull(genreDto.getId());
    }
}
