package sit.tu_varna.bg.core.operationservice.hall;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.operation.hall.add.AddHallOperation;
import sit.tu_varna.bg.api.operation.hall.add.AddHallRequest;
import sit.tu_varna.bg.api.operation.hall.add.AddHallResponse;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;
import sit.tu_varna.bg.entity.Seat;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AddHallService implements AddHallOperation {

    @Transactional
    @Override
    public AddHallResponse process(AddHallRequest request) {
        Hall hall = Hall.builder()
                .name(request.getName())
                .build();

        List<Row> newRows = request.getRows().stream()
                .map(rowDto -> {
                    Row row = Row.builder()
                            .rowNumber(rowDto.getRowNumber())
                            .hall(hall)
                            .build();
                    List<Seat> newSeats = rowDto.getSeats()
                            .stream().map(seatDto -> {
                                Seat seat = Seat.builder()
                                        .seatNumber(seatDto.getSeatNumber())
                                        .row(row)
                                        .isEmptySpace(seatDto.getIsEmpty())
                                        .build();
                                seat.persist();
                                return seat;
                            }).collect(Collectors.toList());
                    row.getSeats().addAll(newSeats);
                    row.persist();
                    return row;
                })
                .collect(Collectors.toList());

        hall.getRows().addAll(newRows);
        hall.setSeatCapacity(hall.getRows().stream()
                .mapToInt(row -> (int) row.getSeats().stream().filter(seat -> !seat.isEmptySpace()).count()).sum());
        hall.persist();

        return AddHallResponse.builder()
                .hallId(hall.getId().toString())
                .build();
    }
}
