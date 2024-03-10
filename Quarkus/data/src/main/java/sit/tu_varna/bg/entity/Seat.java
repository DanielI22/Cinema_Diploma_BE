package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE seats SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Seat extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    private int seatNumber;
    private boolean isEmptySpace = Boolean.FALSE;

    @ManyToOne
    @JoinColumn(name = "row_id")
    private Row row;

    @CreationTimestamp
    private LocalDateTime createdOn;

    private boolean deleted = Boolean.FALSE;
}
