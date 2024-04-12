package sit.tu_varna.bg.core.operationservice.genre;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.dto.GenreDto;
import sit.tu_varna.bg.api.operation.genre.getall.GetAllGenresOperation;
import sit.tu_varna.bg.api.operation.genre.getall.GetAllGenresRequest;
import sit.tu_varna.bg.api.operation.genre.getall.GetAllGenresResponse;
import sit.tu_varna.bg.core.mapper.GenreMapper;
import sit.tu_varna.bg.entity.Genre;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetAllGenresService implements GetAllGenresOperation {
    @Inject
    GenreMapper genreMapper;

    @Transactional
    @Override
    public GetAllGenresResponse process(GetAllGenresRequest request) {
        List<GenreDto> genres = Genre.listAll()
                .stream()
                .filter(Genre.class::isInstance)
                .map(Genre.class::cast)
                .sorted(Comparator.comparing(Genre::getCreatedOn).reversed())
                .map(g -> genreMapper.genreToGenreDto(g))
                .collect(Collectors.toList());

        return GetAllGenresResponse.builder()
                .genres(genres)
                .build();
    }
}
