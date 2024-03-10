package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
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

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL)
    private List<Row> rows;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;

    @CreationTimestamp
    private LocalDateTime createdOn;

    private boolean deleted = Boolean.FALSE;
}
