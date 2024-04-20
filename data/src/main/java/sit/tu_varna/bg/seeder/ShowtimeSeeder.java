package sit.tu_varna.bg.seeder;

import sit.tu_varna.bg.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ShowtimeSeeder {
    public static void seedShowtimes() {
        Hall alpha = Hall.find("name", "Alpha").firstResult();
        Hall omega = Hall.find("name", "Omega").firstResult();

        Cinema varnaCinema = Cinema.find("name", "Cinema Varna").firstResult();

        Movie avengers = Movie.find("title", "The Avengers").firstResult();
        Movie inception = Movie.find("title", "Inception").firstResult();

        Showtime showtime1 = Showtime.builder()
                .cinema(varnaCinema)
                .movie(avengers)
                .hall(alpha)
                .startTime(LocalDateTime.now().plusHours(1))
                .ticketPrice(BigDecimal.valueOf(10))
                .build();

        Showtime showtime2 = Showtime.builder()
                .cinema(varnaCinema)
                .movie(inception)
                .hall(alpha)
                .startTime(LocalDateTime.now().plusHours(2))
                .ticketPrice(BigDecimal.valueOf(10))
                .build();

        Showtime.persist(List.of(showtime1, showtime2));

        for (Seat seat : alpha.getRows().stream().flatMap(row -> row.getSeats().stream()).collect(Collectors.toList())) {
            ShowtimeSeat showtimeSeat1 = ShowtimeSeat.builder().seat(seat).showtime(showtime1).build();
            ShowtimeSeat showtimeSeat2 = ShowtimeSeat.builder().seat(seat).showtime(showtime2).build();
            ShowtimeSeat.persist(List.of(showtimeSeat1, showtimeSeat2));
        }

        Showtime showtime3 = Showtime.builder()
                .cinema(varnaCinema)
                .movie(avengers)
                .hall(omega)
                .startTime(LocalDateTime.now().plusHours(3))
                .ticketPrice(BigDecimal.valueOf(10))
                .build();

        Showtime showtime4 = Showtime.builder()
                .cinema(varnaCinema)
                .movie(inception)
                .hall(omega)
                .startTime(LocalDateTime.now().plusHours(5))
                .ticketPrice(BigDecimal.valueOf(10))
                .build();

        Showtime.persist(List.of(showtime3, showtime4));

        for (Seat seat : omega.getRows().stream().flatMap(row -> row.getSeats().stream()).collect(Collectors.toList())) {
            ShowtimeSeat showtimeSeat3 = ShowtimeSeat.builder().seat(seat).showtime(showtime3).build();
            ShowtimeSeat showtimeSeat4 = ShowtimeSeat.builder().seat(seat).showtime(showtime4).build();
            ShowtimeSeat.persist(List.of(showtimeSeat3, showtimeSeat4));
        }
    }
}
