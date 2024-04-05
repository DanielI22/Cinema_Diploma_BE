package sit.tu_varna.bg.core.operationservice.hall;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.HallCinemaDto;
import sit.tu_varna.bg.api.operation.hall.getall.GetAllHallsOperation;
import sit.tu_varna.bg.api.operation.hall.getall.GetAllHallsRequest;
import sit.tu_varna.bg.api.operation.hall.getall.GetAllHallsResponse;
import sit.tu_varna.bg.core.mapper.HallMapper;
import sit.tu_varna.bg.entity.Hall;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetAllHallsService implements GetAllHallsOperation {
    @Inject
    HallMapper hallMapper;

    @Override
    public GetAllHallsResponse process(GetAllHallsRequest request) {
        List<HallCinemaDto> halls = Hall.findAll().stream()
                .filter(Hall.class::isInstance)
                .map(Hall.class::cast)
                .sorted(Comparator.comparing(Hall::getCreatedOn))
                .map(hallMapper::hallToHallCinemaDto)
                .collect(Collectors.toList());

        return GetAllHallsResponse.builder()
                .halls(halls)
                .build();
    }
}
