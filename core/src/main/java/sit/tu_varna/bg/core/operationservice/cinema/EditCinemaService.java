package sit.tu_varna.bg.core.operationservice.cinema;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.cinema.edit.EditCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.edit.EditCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.edit.EditCinemaResponse;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Hall;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class EditCinemaService implements EditCinemaOperation {

    @Transactional
    @Override
    public EditCinemaResponse process(EditCinemaRequest request) {
        Cinema cinema = (Cinema) Cinema.findByIdOptional(request.getCinemaId())
                .orElseThrow(() -> new ResourceNotFoundException("Cinema does not exist"));
        if (cinema.isPersistent()) {
            cinema.setLocation(request.getLocation());
            cinema.setImageUrl(request.getImageUrl());
            cinema.setName(request.getName());

            cinema.getHalls().stream()
                    .filter(h -> !request.getHalls().contains(h.getId()))
                    .forEach(hall -> {
                        hall.setCinema(null);
                        hall.persist();
                    });

            cinema.getHalls().clear();
            List<Hall> newHalls = request.getHalls().stream()
                    .map(hallId -> {
                        Hall hall = (Hall) Hall.findByIdOptional(hallId)
                                .orElseThrow(() -> new ResourceNotFoundException("Invalid hall id!"));
                        hall.setCinema(cinema);
                        return hall;
                    })
                    .collect(Collectors.toList());

            cinema.getHalls().addAll(newHalls);
            cinema.persist();
        }
        return EditCinemaResponse.builder().cinemaId(cinema.getId().toString()).build();
    }
}
