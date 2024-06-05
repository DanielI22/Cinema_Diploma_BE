package sit.tu_varna.bg.core.operationservice.hall;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.hall.delete.DeleteHallOperation;
import sit.tu_varna.bg.api.operation.hall.delete.DeleteHallRequest;
import sit.tu_varna.bg.api.operation.hall.delete.DeleteHallResponse;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;
import sit.tu_varna.bg.entity.Seat;
import sit.tu_varna.bg.entity.ShowtimeSeat;

import java.util.UUID;

@ApplicationScoped
public class DeleteHallService implements DeleteHallOperation {

    @Transactional
    @Override
    public DeleteHallResponse process(DeleteHallRequest request) {
        UUID hallId = request.getHallId();
        Hall hall = (Hall) Hall.findByIdOptional(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall with id " + hallId + " not found"));
        if (hall.isPersistent()) {
            hall.getRows().stream().map(Row.class::cast)
                    .forEach(r -> r.getSeats().forEach(s -> ShowtimeSeat.findBySeatId(s.getId()).forEach(PanacheEntityBase::delete)));
            hall.getRows().stream().map(Row.class::cast).forEach(r -> r.getSeats().forEach(Seat::delete));
            hall.getRows().forEach(PanacheEntityBase::delete);
            hall.delete();
        }
        return DeleteHallResponse.builder().deleted(true).build();
    }
}
