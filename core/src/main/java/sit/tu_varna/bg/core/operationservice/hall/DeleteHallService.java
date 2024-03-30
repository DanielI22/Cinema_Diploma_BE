package sit.tu_varna.bg.core.operationservice.hall;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.hall.deletehall.DeleteHallOperation;
import sit.tu_varna.bg.api.operation.hall.deletehall.DeleteHallRequest;
import sit.tu_varna.bg.api.operation.hall.deletehall.DeleteHallResponse;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;
import sit.tu_varna.bg.entity.Seat;

@ApplicationScoped
public class DeleteHallService implements DeleteHallOperation {

    @Transactional
    @Override
    public DeleteHallResponse process(DeleteHallRequest request) {
        Hall hall = (Hall) Hall.findByIdOptional(request.getHallId())
                .orElseThrow(() -> new ResourceNotFoundException("Hall does not exist"));
        if (hall.isPersistent()) {
            hall.getRows().stream().map(Row.class::cast).forEach(r -> r.getSeats().forEach(Seat::delete));
            hall.getRows().forEach(PanacheEntityBase::delete);
            hall.delete();
        }
        return DeleteHallResponse.builder().deleted(true).build();
    }
}
