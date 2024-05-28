package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.add.AddShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.add.AddShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.add.AddShowtimeResponse;
import sit.tu_varna.bg.entity.*;
import sit.tu_varna.bg.enums.ShowtimeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

        LocalDateTime startingTime = request.getStartingTime();
        BigDecimal ticketPrice = request.getTicketPrice();
        boolean addNext7Days = request.getAddNext7Days();

        List<UUID> showtimeIds = new ArrayList<>();

        int daysToAdd = addNext7Days ? 7 : 1;
        for (int i = 0; i < daysToAdd; i++) {
            LocalDateTime showtimeStartingTime = startingTime.plusDays(i);

            Showtime showtime = new Showtime();
            showtime.setCinema(cinema);
            showtime.setMovie(movie);
            showtime.setHall(hall);
            showtime.setStartTime(showtimeStartingTime);
            showtime.setTicketPrice(ticketPrice);
            showtime.setType(ShowtimeType.valueOf(request.getType()));

            showtime.persist();

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

            showtimeIds.add(showtime.getId());
        }


        return AddShowtimeResponse.builder().showtimeIds(showtimeIds.stream().map(UUID::toString).collect(Collectors.toList())).build();
    }
}
