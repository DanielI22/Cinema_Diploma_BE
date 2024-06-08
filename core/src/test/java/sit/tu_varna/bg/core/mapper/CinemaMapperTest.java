package sit.tu_varna.bg.core.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.CinemaDto;
import sit.tu_varna.bg.entity.Cinema;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CinemaMapperTest {

    @InjectMocks
    CinemaMapper cinemaMapper;

    private Cinema cinema;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cinema = new Cinema();
        cinema.setId(UUID.randomUUID());
        cinema.setName("Test Cinema");
        cinema.setLocation("Test Location");
        cinema.setImageUrl("http://example.com/image.jpg");
    }

    @Test
    void testCinemaToCinemaDto() {
        CinemaDto cinemaDto = cinemaMapper.cinemaToCinemaDto(cinema);

        assertEquals(cinema.getId().toString(), cinemaDto.getId());
        assertEquals(cinema.getName(), cinemaDto.getName());
        assertEquals(cinema.getLocation(), cinemaDto.getLocation());
        assertEquals(cinema.getImageUrl(), cinemaDto.getImageUrl());
    }
}
