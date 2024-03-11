package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Seat extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    private int seatNumber;
    private boolean isEmptySpace = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "row_id")
    private Row row;
}
