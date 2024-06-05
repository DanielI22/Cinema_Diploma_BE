package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.upcoming.SetUpcomingShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.upcoming.SetUpcomingShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.upcoming.SetUpcomingShowtimeResponse;
import sit.tu_varna.bg.entity.Showtime;

import java.util.UUID;

@ApplicationScoped
public class SetUpcomingShowtimeService implements SetUpcomingShowtimeOperation {

    @Override
    @Transactional
    public SetUpcomingShowtimeResponse process(SetUpcomingShowtimeRequest request) {
        UUID showtimeId = request.getShowtimeId();
        Showtime showtime = (Showtime) Showtime.findByIdOptional(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with id " + showtimeId + " not found"));

        showtime.setCurrent(false);
        showtime.setEnded(false);
        showtime.persist();

        return SetUpcomingShowtimeResponse.builder().upcoming(true).build();
    }
}

