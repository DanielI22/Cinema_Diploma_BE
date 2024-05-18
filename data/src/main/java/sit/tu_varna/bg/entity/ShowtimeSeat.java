package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "showtime_seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShowtimeSeat extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    private boolean isBooked;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    public static List<ShowtimeSeat> findBySeatId(UUID seatId) {
        return find("SELECT st FROM ShowtimeSeat st WHERE st.seat.id = ?1", seatId).list();
    }

    public static List<ShowtimeSeat> findByShowtimeId(UUID showtimeId) {
        return find("SELECT st FROM ShowtimeSeat st WHERE st.showtime.id = ?1", showtimeId).list();
    }

    public static List<ShowtimeSeat> findBySeatIdAndShowtimeId(UUID seatId, UUID showtimeId) {
        return find("SELECT st FROM ShowtimeSeat st WHERE st.seat.id = ?1 AND st.showtime.id = ?2", seatId, showtimeId).list();
    }
}
