package sit.tu_varna.bg.core.operationservice.hall;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.hall.edit.EditHallOperation;
import sit.tu_varna.bg.api.operation.hall.edit.EditHallRequest;
import sit.tu_varna.bg.api.operation.hall.edit.EditHallResponse;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;
import sit.tu_varna.bg.entity.Seat;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EditHallService implements EditHallOperation {

    @Transactional
    @Override
    public EditHallResponse process(EditHallRequest request) {
        Hall hall = (Hall) Hall.findByIdOptional(request.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        hall.setName(request.getName());

        // Remove existing rows and seats
        hall.getRows().forEach(row -> {
            row.getSeats().forEach(Seat::delete);
            row.delete();
        });
        hall.getRows().clear();

        // Recreate rows and seats based on the request
        List<Row> newRows = request.getRows().stream()
                .map(rowDto -> {
                    Row row = new Row();
                    row.setRowNumber(rowDto.getRowNumber());
                    row.setHall(hall);

                    List<Seat> newSeats = rowDto.getSeats().stream()
                            .map(seatDto -> {
                                Seat seat = new Seat();
                                seat.setSeatNumber(seatDto.getSeatNumber());
                                seat.setEmptySpace(seatDto.getIsEmpty());
                                seat.setRow(row);
                                return seat;
                            }).collect(Collectors.toList());

                    row.setSeats(newSeats);
                    return row;
                }).collect(Collectors.toList());

        hall.setRows(newRows);

        hall.setSeatCapacity(hall.getRows().stream()
                .mapToInt(row -> (int) row.getSeats().stream().filter(seat -> !seat.isEmptySpace()).count()).sum());

        hall.persist();

        return EditHallResponse.builder()
                .hallId(hall.getId().toString())
                .build();
    }
}
