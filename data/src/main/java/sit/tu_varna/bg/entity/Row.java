package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "rows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Row extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    private int rowNumber;

    @ManyToOne
    @JoinColumn(name = "hall_id")
    private Hall hall;

    @OneToMany(mappedBy = "row", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Seat> seats = new ArrayList<>();

    public void addSeats(Collection<Seat> seats) {
        this.seats.addAll(seats);
        seats.forEach(s -> s.setRow(this));
    }

    public static long deleteByHallId(UUID hallId) {
        return delete("hall_id", hallId);
    }
}
