package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.ShowtimeDto;
import sit.tu_varna.bg.api.operation.showtime.getcinemaall.GetCinemaShowtimesByDateOperation;
import sit.tu_varna.bg.api.operation.showtime.getcinemaall.GetCinemaShowtimesByDateRequest;
import sit.tu_varna.bg.api.operation.showtime.getcinemaall.GetCinemaShowtimesByDateResponse;
import sit.tu_varna.bg.core.mapper.ShowtimeMapper;
import sit.tu_varna.bg.entity.Showtime;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetCinemaShowtimesByDateService implements GetCinemaShowtimesByDateOperation {
    @Inject
    ShowtimeMapper showtimeMapper;

    @Override
    public GetCinemaShowtimesByDateResponse process(GetCinemaShowtimesByDateRequest request) {
        List<ShowtimeDto> showtimes = Showtime.findByDate(request.getShowtimeDate())
                .stream()
                .filter(s -> s.getCinema().getId().toString().equals(request.getCinemaId()))
                .map(sh -> showtimeMapper.showtimeToShowtimeDto(sh))
                .collect(Collectors.toList());

        return GetCinemaShowtimesByDateResponse.builder()
                .showtimes(showtimes)
                .build();
    }
}
