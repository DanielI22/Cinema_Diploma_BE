package sit.tu_varna.bg.core.operationservice.cinema;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.cinema.add.AddCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.add.AddCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.add.AddCinemaResponse;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Hall;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AddCinemaService implements AddCinemaOperation {
    @Override
    @Transactional
    public AddCinemaResponse process(AddCinemaRequest request) {
        Cinema cinema = Cinema.builder()
                .name(request.getName())
                .location(request.getLocation())
                .imageUrl(request.getImageUrl())
                .build();

        List<Hall> newHalls = request.getHalls().stream()
                .map(hallId -> {
                    Hall hall = (Hall) Hall.findByIdOptional(hallId)
                            .orElseThrow(() -> new ResourceNotFoundException("Hall with id " + hallId + " not found"));
                    hall.setCinema(cinema);
                    return hall;
                })
                .collect(Collectors.toList());

        cinema.getHalls().addAll(newHalls);
        cinema.persist();

        return AddCinemaResponse.builder()
                .cinemaId(cinema.getId().toString())
                .build();
    }
}
