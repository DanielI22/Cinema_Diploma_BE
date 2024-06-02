package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.current.SetCurrentShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.current.SetCurrentShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.current.SetCurrentShowtimeResponse;
import sit.tu_varna.bg.entity.Showtime;

@ApplicationScoped
public class SetCurrentShowtimeService implements SetCurrentShowtimeOperation {

    @Override
    @Transactional
    public SetCurrentShowtimeResponse process(SetCurrentShowtimeRequest request) {
        Showtime showtime = (Showtime) Showtime.findByIdOptional(request.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime does not exist"));

        showtime.setCurrent(true);
        showtime.setEnded(false);
        showtime.persist();

        return SetCurrentShowtimeResponse.builder().current(true).build();
    }
}

