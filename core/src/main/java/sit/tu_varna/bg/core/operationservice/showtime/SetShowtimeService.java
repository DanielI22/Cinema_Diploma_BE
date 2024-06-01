package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.showtime.set.SetShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.set.SetShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.set.SetShowtimeResponse;
import sit.tu_varna.bg.entity.Showtime;

@ApplicationScoped
public class SetShowtimeService implements SetShowtimeOperation {

    @Override
    @Transactional
    public SetShowtimeResponse process(SetShowtimeRequest request) {
        Showtime showtime = (Showtime) Showtime.findByIdOptional(request.getShowtimeId())
                .orElseThrow(() -> new ResourceNotFoundException("Showtime does not exist"));

        showtime.setCurrent(true);
        showtime.persist();

        return SetShowtimeResponse.builder().set(true).build();
    }
}

