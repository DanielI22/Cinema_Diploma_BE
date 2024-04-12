package sit.tu_varna.bg.core.operationservice.cinema;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.CinemaHallDto;
import sit.tu_varna.bg.api.operation.cinema.getHalls.GetCinemaHallsOperation;
import sit.tu_varna.bg.api.operation.cinema.getHalls.GetCinemaHallsRequest;
import sit.tu_varna.bg.api.operation.cinema.getHalls.GetCinemaHallsResponse;
import sit.tu_varna.bg.api.operation.hall.getavailable.GetAvailableHallsResponse;
import sit.tu_varna.bg.core.mapper.HallMapper;
import sit.tu_varna.bg.entity.Hall;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetCinemaHallsService implements GetCinemaHallsOperation {
    @Inject
    HallMapper hallMapper;

    @Override
    public GetCinemaHallsResponse process(GetCinemaHallsRequest request) {
        List<Hall> cinemaHalls = Hall.findByCinemaId(request.getCinemaId());
        List<CinemaHallDto> halls;

        if (cinemaHalls.isEmpty()) {
            halls = Collections.emptyList();
        } else {
            halls = cinemaHalls.stream()
                    .map(hallMapper::hallToCinemaHallDto)
                    .collect(Collectors.toList());
        }
        return GetCinemaHallsResponse.builder()
                .halls(halls)
                .build();
    }
}
