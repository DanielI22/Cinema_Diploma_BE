package sit.tu_varna.bg.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@SoftDelete
public class Hall {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private String id;

    private String name;
    private int seatCapacity;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @ManyToOne
    @JoinColumn(name = "cinema_id")
    private Cinema cinema;
}
