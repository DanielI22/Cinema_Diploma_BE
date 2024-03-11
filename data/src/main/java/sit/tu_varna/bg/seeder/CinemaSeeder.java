package sit.tu_varna.bg.seeder;

import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Row;
import sit.tu_varna.bg.entity.Seat;

import java.time.Instant;
import java.util.List;

public class CinemaSeeder {
    public static void seedCinema() {
        Seat seat11 = Seat.builder()
                .seatNumber(1)
                .build();

        Seat seat12 = Seat.builder()
                .seatNumber(2)
                .build();

        Seat seat21 = Seat.builder()
                .seatNumber(1)
                .build();

        Seat seat22 = Seat.builder()
                .seatNumber(2)
                .build();

        Seat seat211 = Seat.builder()
                .seatNumber(1)
                .build();

        Seat seat212 = Seat.builder()
                .seatNumber(2)
                .build();

        Seat seat221 = Seat.builder()
                .seatNumber(1)
                .build();

        Seat seat222 = Seat.builder()
                .seatNumber(2)
                .build();

        Seat.persist(List.of(seat11, seat12, seat21, seat22, seat211, seat212, seat221, seat222));

        Row row1 = Row.builder()
                .rowNumber(1)
                .build();

        row1.addSeats(List.of(seat11, seat12, seat21, seat22));

        Row row2 = Row.builder()
                .rowNumber(2)
                .build();

        row2.addSeats(List.of(seat211, seat212, seat221, seat222));

        Row.persist(List.of(row1, row2));

        Hall omega = Hall.builder()
                .name("Omega")
                .seatCapacity(4)
                .createdOn(Instant.now())
                .build();

        Hall.persist(omega);
        omega.addRows(List.of(row1, row2));

        Seat seat311 = Seat.builder()
                .seatNumber(1)
                .build();

        Seat seat312 = Seat.builder()
                .seatNumber(2)
                .build();

        Seat seat321 = Seat.builder()
                .seatNumber(1)
                .build();

        Seat seat322 = Seat.builder()
                .seatNumber(2)
                .build();

        Seat seat411 = Seat.builder()
                .seatNumber(1)
                .build();

        Seat seat412 = Seat.builder()
                .seatNumber(2)
                .build();

        Seat seat421 = Seat.builder()
                .seatNumber(1)
                .build();

        Seat seat422 = Seat.builder()
                .seatNumber(2)
                .build();

        Seat.persist(List.of(seat311, seat312, seat321, seat322, seat411, seat412, seat421, seat422));

        Row row21 = Row.builder()
                .rowNumber(1)
                .build();

        row21.addSeats(List.of(seat311, seat312, seat321, seat322));

        Row row22 = Row.builder()
                .rowNumber(2)
                .build();

        row22.addSeats(List.of(seat411, seat412, seat421, seat422));

        Row.persist(List.of(row21, row22));

        Hall alpha = Hall.builder()
                .name("Alpha")
                .seatCapacity(4)
                .createdOn(Instant.now())
                .build();

        Hall.persist(alpha);
        alpha.addRows(List.of(row21, row22));

        Cinema cinemaVarna = Cinema.builder()
                .name("Cinema Varna")
                .location("Varna")
                .imageUrl("testImageUrl")
                .build();

        cinemaVarna.addHall(alpha);
        cinemaVarna.addHall(omega);

        Cinema.persist(cinemaVarna);
    }
}
