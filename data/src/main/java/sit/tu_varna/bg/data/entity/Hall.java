package sit.tu_varna.bg.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;

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
@SoftDelete
public class Hall {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private int seatCapacity;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @OneToMany(mappedBy = "hall", cascade = CascadeType.ALL)
    private List<Row> rows;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;
}
