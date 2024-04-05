package sit.tu_varna.bg.core.operationservice.cinema;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.CinemaDto;
import sit.tu_varna.bg.api.operation.cinema.getall.GetAllCinemasOperation;
import sit.tu_varna.bg.api.operation.cinema.getall.GetAllCinemasRequest;
import sit.tu_varna.bg.api.operation.cinema.getall.GetAllCinemasResponse;
import sit.tu_varna.bg.core.mapper.CinemaMapper;
import sit.tu_varna.bg.entity.Cinema;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetAllCinemasService implements GetAllCinemasOperation {
    @Inject
    CinemaMapper cinemaMapper;

    @Override
    public GetAllCinemasResponse process(GetAllCinemasRequest request) {
        List<CinemaDto> cinemas = Cinema.listAll()
                .stream()
                .filter(Cinema.class::isInstance)
                .map(Cinema.class::cast)
                .sorted(Comparator.comparing(Cinema::getCreatedOn))
                .map(c -> cinemaMapper.cinemaToCinemaDto(c))
                .collect(Collectors.toList());
        return GetAllCinemasResponse.builder()
                .cinemas(cinemas)
                .build();
    }
}
