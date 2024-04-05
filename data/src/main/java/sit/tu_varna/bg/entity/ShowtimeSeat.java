package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import sit.tu_varna.bg.enums.SeatStatus;

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

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private SeatStatus status = SeatStatus.AVAILABLE;

    @ManyToOne
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    public static List<ShowtimeSeat> findBySeatId(UUID seatId) {
        return find("SELECT st FROM ShowtimeSeat st WHERE st.seat.id = ?1", seatId).list();
    }
}
