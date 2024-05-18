package sit.tu_varna.bg.core.operationservice.hall;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.HallDto;
import sit.tu_varna.bg.api.operation.hall.getavailable.GetAvailableHallsOperation;
import sit.tu_varna.bg.api.operation.hall.getavailable.GetAvailableHallsRequest;
import sit.tu_varna.bg.api.operation.hall.getavailable.GetAvailableHallsResponse;
import sit.tu_varna.bg.core.mapper.HallMapper;
import sit.tu_varna.bg.entity.Hall;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetAvailableHallsService implements GetAvailableHallsOperation {
    @Inject
    HallMapper hallMapper;

    @Override
    public GetAvailableHallsResponse process(GetAvailableHallsRequest request) {
        List<Hall> availableHalls = Hall.findAvailable();
        List<HallDto> halls;

        if (availableHalls.isEmpty()) {
            halls = Collections.emptyList();
        } else {
            halls = availableHalls.stream()
                    .map(hallMapper::hallToHallDto)
                    .collect(Collectors.toList());
        }
        return GetAvailableHallsResponse.builder()
                .halls(halls)
                .build();
    }
}
