package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "cinemas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE cinemas SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Cinema extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    private String name;
    private String location;
    private String imageUrl;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Hall> halls;

    @CreationTimestamp
    private LocalDateTime createdOn;

    private boolean deleted = Boolean.FALSE;
}
