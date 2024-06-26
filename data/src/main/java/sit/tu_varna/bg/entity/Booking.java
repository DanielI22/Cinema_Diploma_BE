package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import sit.tu_varna.bg.enums.BookingStatus;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true, nullable = false)
    private String shortCode;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BookingStatus status = BookingStatus.AVAILABLE;

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Builder.Default
    private List<Ticket> tickets = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    private Instant createdOn;

    public static List<Booking> findByShowtimeId(UUID showtimeId) {
        return find("SELECT b FROM Booking b WHERE b.showtime.id = ?1", showtimeId).list();
    }

    public static List<Booking> findByUserId(UUID userId) {
        return find("SELECT b FROM Booking b WHERE b.user.id = ?1", userId).list();
    }

    public static Optional<Booking> findByShortCode(String bookingShortCode) {
        return find("SELECT b FROM Booking b WHERE b.shortCode = ?1", bookingShortCode).firstResultOptional();
    }
}
