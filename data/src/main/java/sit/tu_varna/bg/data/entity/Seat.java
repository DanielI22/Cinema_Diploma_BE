package sit.tu_varna.bg.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import sit.tu_varna.bg.data.enums.SeatStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "seats")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@SoftDelete
public class Seat {
    @Id
    @GeneratedValue
    private UUID id;

    private int seatNumber;
    private boolean isEmptySpace;

    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "row_id")
    private Row row;
}
