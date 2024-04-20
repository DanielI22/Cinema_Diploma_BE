package sit.tu_varna.bg.core.operationservice.showtime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.edit.EditShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.edit.EditShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.edit.EditShowtimeResponse;
import sit.tu_varna.bg.entity.*;

import java.util.UUID;

@ApplicationScoped
public class EditShowtimeService implements EditShowtimeOperation {

    @Transactional
    @Override
    public EditShowtimeResponse process(EditShowtimeRequest request) {
        Showtime showtime = (Showtime) Showtime.findByIdOptional(request.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime does not exist"));

        if (showtime.isPersistent()) {
            UUID cinemaId = request.getCinemaId();
            UUID movieId = request.getMovieId();
            UUID hallId = request.getHallId();

            Cinema cinema = (Cinema) Cinema.findByIdOptional(cinemaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cinema not found with ID: " + cinemaId));
            Movie movie = (Movie) Movie.findByIdOptional(movieId)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId));
            Hall hall = (Hall) Hall.findByIdOptional(hallId)
                    .orElseThrow(() -> new ResourceNotFoundException("Hall not found with ID: " + hallId));

            // renew seats state if hall is changed
            if (showtime.getHall() != hall) {
                ShowtimeSeat.findByShowtimeId(showtime.getId()).forEach(PanacheEntityBase::delete);
                for (Row row : hall.getRows()) {
                    for (Seat seat : row.getSeats()) {
                        if (!seat.isEmptySpace()) {
                            ShowtimeSeat showtimeSeat = ShowtimeSeat.builder()
                                    .seat(seat)
                                    .showtime(showtime)
                                    .build();
                            showtimeSeat.persist();
                        }
                    }
                }
            }

            showtime.setCinema(cinema);
            showtime.setMovie(movie);
            showtime.setHall(hall);
            showtime.setStartTime(request.getStartingTime());
            showtime.setTicketPrice(request.getTicketPrice());

            showtime.persist();
        }
        return EditShowtimeResponse.builder().showtimeId(showtime.getId().toString()).build();
    }
}
