package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.util.*;

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

    @OneToMany(mappedBy = "cinema")
    @Builder.Default
    private Set<Hall> halls = new HashSet<>();

    @CreationTimestamp
    private Instant createdOn;

    private boolean deleted = Boolean.FALSE;
}
