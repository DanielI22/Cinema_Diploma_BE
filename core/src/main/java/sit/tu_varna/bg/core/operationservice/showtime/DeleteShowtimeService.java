package sit.tu_varna.bg.core.operationservice.showtime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.delete.DeleteShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.delete.DeleteShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.delete.DeleteShowtimeResponse;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.entity.ShowtimeSeat;

@ApplicationScoped
public class DeleteShowtimeService implements DeleteShowtimeOperation {

    @Transactional
    @Override
    public DeleteShowtimeResponse process(DeleteShowtimeRequest request) {
        Showtime showtime = (Showtime) Showtime.findByIdOptional(request.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime does not exist"));
        if (showtime.isPersistent()) {
            ShowtimeSeat.findByShowtimeId(showtime.getId()).forEach(PanacheEntityBase::delete);
            showtime.delete();
        }
        return DeleteShowtimeResponse.builder().deleted(true).build();
    }
}
