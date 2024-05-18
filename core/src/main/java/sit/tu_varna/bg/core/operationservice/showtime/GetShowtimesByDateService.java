package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.ShowtimeDto;
import sit.tu_varna.bg.api.operation.showtime.getall.GetShowtimesByDateOperation;
import sit.tu_varna.bg.api.operation.showtime.getall.GetShowtimesByDateRequest;
import sit.tu_varna.bg.api.operation.showtime.getall.GetShowtimesByDateResponse;
import sit.tu_varna.bg.core.mapper.ShowtimeMapper;
import sit.tu_varna.bg.entity.Showtime;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetShowtimesByDateService implements GetShowtimesByDateOperation {
    @Inject
    ShowtimeMapper showtimeMapper;

    @Override
    public GetShowtimesByDateResponse process(GetShowtimesByDateRequest request) {
        List<ShowtimeDto> showtimes = Showtime.findByDate(request.getShowtimeDate())
                .stream()
                .sorted(Comparator.comparing(Showtime::getStartTime))
                .map(sh -> showtimeMapper.showtimeToShowtimeDto(sh))
                .collect(Collectors.toList());

        return GetShowtimesByDateResponse.builder()
                .showtimes(showtimes)
                .build();
    }
}
