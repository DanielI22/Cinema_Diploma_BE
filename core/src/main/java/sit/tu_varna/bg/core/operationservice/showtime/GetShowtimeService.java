package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.get.GetShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.get.GetShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.get.GetShowtimeResponse;
import sit.tu_varna.bg.core.mapper.ShowtimeMapper;
import sit.tu_varna.bg.entity.Showtime;

import java.util.UUID;

@ApplicationScoped
public class GetShowtimeService implements GetShowtimeOperation {
    @Inject
    ShowtimeMapper showtimeMapper;

    @Override
    public GetShowtimeResponse process(GetShowtimeRequest request) {
        UUID showtimeId = request.getShowtimeId();
        Showtime showtime = (Showtime) Showtime.findByIdOptional(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with id " + showtimeId + " not found"));

        return GetShowtimeResponse.builder()
                .showtime(showtimeMapper.showtimeToShowtimeDto(showtime))
                .cinemaId(showtime.getCinema().getId())
                .hallId(showtime.getHall().getId())
                .movieId(showtime.getMovie().getId())
                .build();
    }
}
