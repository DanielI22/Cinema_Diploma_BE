package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.ShowtimeDto;
import sit.tu_varna.bg.api.operation.showtime.gethallbydate.GetShowtimesByHallOperation;
import sit.tu_varna.bg.api.operation.showtime.gethallbydate.GetShowtimesByHallRequest;
import sit.tu_varna.bg.api.operation.showtime.gethallbydate.GetShowtimesByHallResponse;
import sit.tu_varna.bg.core.mapper.ShowtimeMapper;
import sit.tu_varna.bg.entity.Showtime;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetShowtimesByHallService implements GetShowtimesByHallOperation {
    @Inject
    ShowtimeMapper showtimeMapper;

    @Override
    public GetShowtimesByHallResponse process(GetShowtimesByHallRequest request) {
        List<ShowtimeDto> showtimes = Showtime.findByDate(request.getShowtimeDate())
                .stream()
                .filter(showtime -> showtime.getHall().getId().equals(request.getHallId()))
                .sorted(Comparator.comparing(Showtime::getStartTime))
                .map(sh -> showtimeMapper.showtimeToShowtimeDto(sh))
                .collect(Collectors.toList());

        return GetShowtimesByHallResponse.builder()
                .showtimes(showtimes)
                .build();
    }
}
