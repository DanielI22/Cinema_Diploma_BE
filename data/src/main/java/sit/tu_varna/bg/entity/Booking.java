package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import sit.tu_varna.bg.enums.BookingStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE bookings SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Booking extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BookingStatus status = BookingStatus.AVAILABLE;

    @OneToMany(mappedBy = "booking")
    @Builder.Default
    private Set<ShowtimeSeat> showtimeSeats = new HashSet<>();

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    private Instant createdOn;

    private boolean deleted = Boolean.FALSE;
}
