package sit.tu_varna.bg.core.operationservice.cinema;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.cinema.get.GetCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.get.GetCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.get.GetCinemaResponse;
import sit.tu_varna.bg.core.mapper.CinemaMapper;
import sit.tu_varna.bg.core.mapper.HallMapper;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Hall;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetCinemaService implements GetCinemaOperation {
    @Inject
    CinemaMapper cinemaMapper;
    @Inject
    HallMapper hallMapper;

    @Transactional
    @Override
    public GetCinemaResponse process(GetCinemaRequest request) {
        UUID cinemaId = request.getCinemaId();
        Cinema cinema = (Cinema) Cinema.findByIdOptional(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema with id " + cinemaId + " not found"));
        Set<Hall> halls = cinema.getHalls();
        return GetCinemaResponse.builder()
                .cinema(cinemaMapper.cinemaToCinemaDto(cinema))
                .halls(halls.stream().map(hallMapper::hallToCinemaHallDto).collect(Collectors.toList()))
                .build();
    }
}
