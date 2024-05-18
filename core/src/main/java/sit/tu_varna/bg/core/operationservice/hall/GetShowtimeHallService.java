package sit.tu_varna.bg.core.operationservice.hall;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.hall.getshowtimehall.GetShowtimeHallOperation;
import sit.tu_varna.bg.api.operation.hall.getshowtimehall.GetShowtimeHallRequest;
import sit.tu_varna.bg.api.operation.hall.getshowtimehall.GetShowtimeHallResponse;
import sit.tu_varna.bg.core.mapper.HallMapper;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;
import sit.tu_varna.bg.entity.Showtime;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetShowtimeHallService implements GetShowtimeHallOperation {
    @Inject
    HallMapper hallMapper;

    @Transactional
    @Override
    public GetShowtimeHallResponse process(GetShowtimeHallRequest request) {
        UUID showtimeId = request.getShowtimeId();
        Showtime showtime = (Showtime) Showtime.findByIdOptional(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with id " + showtimeId + " not found"));
        Hall hall = showtime.getHall();
        List<Row> rows = hall.getRows();
        return GetShowtimeHallResponse.builder()
                .hall(hallMapper.hallToHallDto(hall))
                .rows(rows.stream().map(row -> hallMapper.rowToShowtimeRowDto(row, showtimeId)).collect(Collectors.toList()))
                .build();
    }
}
