package sit.tu_varna.bg.core.operationservice.showtime;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.edit.EditShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.edit.EditShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.edit.EditShowtimeResponse;
import sit.tu_varna.bg.entity.*;
import sit.tu_varna.bg.enums.ShowtimeType;

import java.util.UUID;

@ApplicationScoped
public class EditShowtimeService implements EditShowtimeOperation {

    @Transactional
    @Override
    public EditShowtimeResponse process(EditShowtimeRequest request) {
        UUID showtimeId = request.getShowtimeId();
        Showtime showtime = (Showtime) Showtime.findByIdOptional(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with id " + showtimeId + " not found"));

        if (showtime.isPersistent()) {
            UUID cinemaId = request.getCinemaId();
            UUID movieId = request.getMovieId();
            UUID hallId = request.getHallId();

            Cinema cinema = (Cinema) Cinema.findByIdOptional(cinemaId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cinema with id " + cinemaId + " not found"));
            Movie movie = (Movie) Movie.findByIdOptional(movieId)
                    .orElseThrow(() -> new ResourceNotFoundException("Movie with id " + movieId + " not found"));
            Hall hall = (Hall) Hall.findByIdOptional(hallId)
                    .orElseThrow(() -> new ResourceNotFoundException("Hall with id " + hallId + " not found"));

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
            showtime.setType(ShowtimeType.valueOf(request.getType()));

            showtime.persist();
        }
        return EditShowtimeResponse.builder().showtimeId(showtime.getId().toString()).build();
    }
}
