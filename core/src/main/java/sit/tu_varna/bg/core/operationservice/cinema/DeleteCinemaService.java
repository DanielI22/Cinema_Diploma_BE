package sit.tu_varna.bg.core.operationservice.cinema;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.cinema.delete.DeleteCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.delete.DeleteCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.delete.DeleteCinemaResponse;
import sit.tu_varna.bg.entity.Cinema;

@ApplicationScoped
public class DeleteCinemaService implements DeleteCinemaOperation {

    @Transactional
    @Override
    public DeleteCinemaResponse process(DeleteCinemaRequest request) {
        Cinema cinema = (Cinema) Cinema.findByIdOptional(request.getCinemaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cinema does not exist"));
        if (cinema.isPersistent()) {
            cinema.delete();
        }
        return DeleteCinemaResponse.builder().deleted(true).build();
    }
}
