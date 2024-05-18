package sit.tu_varna.bg.core.operationservice.showtime;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.ShowtimeDto;
import sit.tu_varna.bg.api.operation.showtime.getmovieall.GetMovieShowtimesByDateOperation;
import sit.tu_varna.bg.api.operation.showtime.getmovieall.GetMovieShowtimesByDateRequest;
import sit.tu_varna.bg.api.operation.showtime.getmovieall.GetMovieShowtimesByDateResponse;
import sit.tu_varna.bg.core.mapper.ShowtimeMapper;
import sit.tu_varna.bg.entity.Showtime;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetMovieShowtimesByDateService implements GetMovieShowtimesByDateOperation {
    @Inject
    ShowtimeMapper showtimeMapper;

    @Override
    public GetMovieShowtimesByDateResponse process(GetMovieShowtimesByDateRequest request) {
        List<ShowtimeDto> showtimes = Showtime.findByDate(request.getShowtimeDate())
                .stream()
                .filter(s -> s.getMovie().getId().toString().equals(request.getMovieId()))
                .sorted(Comparator.comparing(Showtime::getStartTime))
                .map(sh -> showtimeMapper.showtimeToShowtimeDto(sh))
                .collect(Collectors.toList());

        return GetMovieShowtimesByDateResponse.builder()
                .showtimes(showtimes)
                .build();
    }
}
