package sit.tu_varna.bg.core.operationservice.cinema;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.HallDto;
import sit.tu_varna.bg.api.operation.cinema.gethalls.GetCinemaHallsOperation;
import sit.tu_varna.bg.api.operation.cinema.gethalls.GetCinemaHallsRequest;
import sit.tu_varna.bg.api.operation.cinema.gethalls.GetCinemaHallsResponse;
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
        List<HallDto> halls;

        if (cinemaHalls.isEmpty()) {
            halls = Collections.emptyList();
        } else {
            halls = cinemaHalls.stream()
                    .map(hallMapper::hallToHallDto)
                    .collect(Collectors.toList());
        }
        return GetCinemaHallsResponse.builder()
                .halls(halls)
                .build();
    }
}
