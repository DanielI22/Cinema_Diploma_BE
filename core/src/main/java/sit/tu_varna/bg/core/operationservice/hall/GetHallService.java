package sit.tu_varna.bg.core.operationservice.hall;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.hall.get.GetHallOperation;
import sit.tu_varna.bg.api.operation.hall.get.GetHallRequest;
import sit.tu_varna.bg.api.operation.hall.get.GetHallResponse;
import sit.tu_varna.bg.core.mapper.HallMapper;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetHallService implements GetHallOperation {
    @Inject
    HallMapper hallMapper;

    @Transactional
    @Override
    public GetHallResponse process(GetHallRequest request) {
        UUID hallId = request.getHallId();
        Hall hall = (Hall) Hall.findByIdOptional(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall with id " + hallId + " not found"));
        List<Row> rows = hall.getRows();
        return GetHallResponse.builder()
                .hall(hallMapper.hallToHallDto(hall))
                .rows(rows.stream().map(hallMapper::rowToRowDto).collect(Collectors.toList()))
                .build();
    }
}
