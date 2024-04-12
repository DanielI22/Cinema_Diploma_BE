package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.add.AddShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.add.AddShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.add.AddShowtimeResponse;
import sit.tu_varna.bg.entity.*;

import java.util.UUID;

@ApplicationScoped
public class AddShowtimeService implements AddShowtimeOperation {

    @Transactional
    @Override
    public AddShowtimeResponse process(AddShowtimeRequest request) {
        UUID cinemaId = request.getCinemaId();
        UUID movieId = request.getMovieId();
        UUID hallId = request.getHallId();

        Cinema cinema = (Cinema) Cinema.findByIdOptional(cinemaId)
                .orElseThrow(() -> new ResourceNotFoundException("Cinema not found with ID: " + cinemaId));
        Movie movie = (Movie) Movie.findByIdOptional(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie not found with ID: " + movieId));
        Hall hall = (Hall) Hall.findByIdOptional(hallId)
                .orElseThrow(() -> new ResourceNotFoundException("Hall not found with ID: " + hallId));

        Showtime showtime = new Showtime();
        showtime.setCinema(cinema);
        showtime.setMovie(movie);
        showtime.setHall(hall);
        showtime.setStartTime(request.getStartingTime());
        showtime.setTicketPrice(request.getTicketPrice());

        showtime.persist();

        for (Row row : hall.getRows()) {
            for (Seat seat : row.getSeats()) {
                ShowtimeSeat showtimeSeat = ShowtimeSeat.builder()
                        .seat(seat)
                        .showtime(showtime)
                        .build();
                showtimeSeat.persist();
            }
        }

        return AddShowtimeResponse.builder().showtimeId(showtime.getId().toString()).build();
    }
}
