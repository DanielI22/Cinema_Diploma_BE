package sit.tu_varna.bg.core.operationservice.cinema;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.cinema.delete.DeleteCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.delete.DeleteCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.delete.DeleteCinemaResponse;
import sit.tu_varna.bg.entity.Cinema;

import java.util.UUID;

@ApplicationScoped
public class DeleteCinemaService implements DeleteCinemaOperation {

    @Transactional
    @Override
    public DeleteCinemaResponse process(DeleteCinemaRequest request) {
        UUID cinemaId = request.getCinemaId();
        Cinema cinema = (Cinema) Cinema.findByIdOptional(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema with id " + cinemaId + " not found"));
        if (cinema.isPersistent()) {
            cinema.delete();
        }
        return DeleteCinemaResponse.builder().deleted(true).build();
    }
}
