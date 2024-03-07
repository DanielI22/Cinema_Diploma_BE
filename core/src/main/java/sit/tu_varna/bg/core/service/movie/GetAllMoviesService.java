package sit.tu_varna.bg.core.service.movie;

import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import sit.tu_varna.bg.api.dto.MovieDto;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesOperation;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesRequest;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesResponse;
import sit.tu_varna.bg.data.repository.MovieRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllMoviesService implements GetAllMoviesOperation {
    private final MovieRepository movieRepository;
    private final ConversionService conversionService;

    @Override
    public GetAllMoviesResponse process(GetAllMoviesRequest request) {
        List<MovieDto> movies = movieRepository.findAll()
                .stream()
                .map(m -> conversionService.convert(m, MovieDto.class))
                .collect(Collectors.toList());
        return GetAllMoviesResponse.builder()
                .movies(movies)
                .build();
    }
}
