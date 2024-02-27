package sit.tu_varna.bg.data.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@SoftDelete
public class Cinema {

    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private String id;

    private String name;
    private String location;

    @CreationTimestamp
    private LocalDateTime createdOn;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Hall> halls;
}