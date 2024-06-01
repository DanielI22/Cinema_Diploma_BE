package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.end.EndShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.end.EndShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.end.EndShowtimeResponse;
import sit.tu_varna.bg.entity.Showtime;

@ApplicationScoped
public class EndShowtimeService implements EndShowtimeOperation {

    @Override
    @Transactional
    public EndShowtimeResponse process(EndShowtimeRequest request) {
        Showtime showtime = (Showtime) Showtime.findByIdOptional(request.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime does not exist"));

        showtime.setEnded(true);
        showtime.persist();

        return EndShowtimeResponse.builder().ended(true).build();
    }
}
