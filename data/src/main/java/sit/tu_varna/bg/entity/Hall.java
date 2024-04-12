package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "halls")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE halls SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Hall extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private int seatCapacity;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Row> rows = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    @CreationTimestamp
    private Instant createdOn;

    private boolean deleted = Boolean.FALSE;

    public void addRows(Collection<Row> rows) {
        this.rows.addAll(rows);
        rows.forEach(r -> r.setHall(this));
    }

    public static List<Hall> findAvailable() {
        return find("SELECT h FROM Hall h WHERE h.cinema IS NULL").list();
    }

    public static List<Hall> findByCinemaId(UUID cinemaId) {
        return find("SELECT h FROM Hall h WHERE h.cinema.id = ?1", cinemaId).list();
    }
}
