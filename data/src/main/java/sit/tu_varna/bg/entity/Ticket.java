package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.panache.common.Page;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import sit.tu_varna.bg.enums.TicketStatus;
import sit.tu_varna.bg.enums.TicketType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String shortCode;

    private BigDecimal price;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "showtime_seat_id", nullable = false)
    private ShowtimeSeat showtimeSeat;

    @ManyToOne
    @JoinColumn(name = "showtime_id", nullable = false)
    private Showtime showtime;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private TicketType ticketType;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    @CreationTimestamp
    private Instant createdOn;

    public static List<Ticket> findByShowtimeId(UUID showtimeId) {
        return find("SELECT t FROM Ticket t WHERE t.showtime.id = ?1", showtimeId).list();
    }

    public static List<Ticket> findByUserId(UUID userId) {
        return find("SELECT t FROM Ticket t WHERE t.user.id = ?1", userId).list();
    }

    public static Optional<Ticket> findByShortCode(String ticketShortCode) {
        return find("SELECT t FROM Ticket t WHERE t.shortCode = ?1", ticketShortCode).firstResultOptional();
    }

    public static List<Ticket> findLastSoldTickets(UUID userId, UUID cinemaId, int index, int limit) {
        return find("SELECT t FROM Ticket t JOIN t.showtime s WHERE t.user.id = ?1 AND s.cinema.id = ?2 AND t.ticketStatus = ?3 ORDER BY t.createdOn DESC",
                userId, cinemaId, TicketStatus.PURCHASED)
                .page(Page.of(index, limit))
                .list();
    }

    public static long countTickets(UUID userId, UUID cinemaId) {
        return count("WHERE user.id = ?1 AND showtime.cinema.id = ?2 AND ticketStatus = ?3",
                userId, cinemaId, TicketStatus.PURCHASED);
    }
}
