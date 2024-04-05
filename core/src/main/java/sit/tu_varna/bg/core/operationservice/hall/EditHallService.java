package sit.tu_varna.bg.core.operationservice.hall;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.dto.RowDto;
import sit.tu_varna.bg.api.dto.SeatDto;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.hall.edit.EditHallOperation;
import sit.tu_varna.bg.api.operation.hall.edit.EditHallRequest;
import sit.tu_varna.bg.api.operation.hall.edit.EditHallResponse;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;
import sit.tu_varna.bg.entity.Seat;
import sit.tu_varna.bg.entity.ShowtimeSeat;

import java.util.Collection;

@ApplicationScoped
public class EditHallService implements EditHallOperation {

    @Transactional
    @Override
    public EditHallResponse process(EditHallRequest request) {
        Hall hall = (Hall) Hall.findByIdOptional(request.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found"));

        hall.setName(request.getName());

        // Explicitly remove existing rows and seats
        hall.getRows().stream().map(Row.class::cast)
                .forEach(r -> r.getSeats().forEach(s -> ShowtimeSeat.findBySeatId(s.getId()).forEach(PanacheEntityBase::delete)));
        hall.getRows().stream().map(Row.class::cast).forEach(r -> r.getSeats().forEach(Seat::delete));
        hall.getRows().forEach(PanacheEntityBase::delete);
        hall.getRows().clear();

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
            row.persist();
            hall.getRows().add(row);
        }

        hall.persist();
        // Update the hall's seat capacity
        hall.setSeatCapacity(hall.getRows().stream()
                .flatMapToInt(row -> row.getSeats().stream().filter(seat -> !seat.isEmptySpace()).mapToInt(s -> 1)).sum());

        return EditHallResponse.builder()
                .hallId(hall.getId().toString())
                .build();
    }
}
