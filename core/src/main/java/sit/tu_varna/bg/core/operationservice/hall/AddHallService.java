package sit.tu_varna.bg.core.operationservice.hall;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.dto.RowDto;
import sit.tu_varna.bg.api.dto.SeatDto;
import sit.tu_varna.bg.api.operation.hall.add.AddHallOperation;
import sit.tu_varna.bg.api.operation.hall.add.AddHallRequest;
import sit.tu_varna.bg.api.operation.hall.add.AddHallResponse;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;
import sit.tu_varna.bg.entity.Seat;

import java.util.Collection;

@ApplicationScoped
public class AddHallService implements AddHallOperation {

    @Transactional
    @Override
    public AddHallResponse process(AddHallRequest request) {
        Hall hall = Hall.builder()
                .name(request.getName())
                .build();

        Collection<RowDto> rows = request.getRows();
        for (RowDto rowDto : rows) {
            Row row = new Row();
            row.setRowNumber(rowDto.getRowNumber());
            row.setHall(hall);

            Collection<SeatDto> newSeats = rowDto.getSeats();
            for (SeatDto seatDto : newSeats) {
                Seat seat = new Seat();
                seat.setSeatNumber(seatDto.getSeatNumber());
                seat.setEmptySpace(seatDto.getIsEmpty());
                seat.setRow(row);
                row.getSeats().add(seat);
            }
            hall.getRows().add(row);
        }

        hall.setSeatCapacity(hall.getRows().stream()
                .mapToInt(row -> (int) row.getSeats().stream().filter(seat -> !seat.isEmptySpace()).count()).sum());
        hall.persist();

        return AddHallResponse.builder()
                .hallId(hall.getId().toString())
                .build();
    }
}
