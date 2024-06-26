package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import sit.tu_varna.bg.enums.ShowtimeType;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "showtimes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE showtimes SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Showtime extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    private LocalDateTime startTime;

    private BigDecimal ticketPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShowtimeType type;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "hall_id", nullable = false)
    private Hall hall;

    @ManyToOne
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    private boolean isCurrent = Boolean.FALSE;

    private boolean isEnded = Boolean.FALSE;

    @CreationTimestamp
    private Instant createdOn;

    private boolean deleted = Boolean.FALSE;

    public static List<Showtime> findByDate(LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        return list("startTime between ?1 and ?2", startOfDay, endOfDay);
    }

    public static List<Showtime> findStartingSoon(LocalDateTime now, LocalDateTime threshold) {
        return list("startTime between ?1 and ?2", now, threshold);
    }
}
