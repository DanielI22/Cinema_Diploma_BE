package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import sit.tu_varna.bg.enums.Sentiment;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "reviews")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(length = 1000)
    private String reviewText;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @CreationTimestamp
    private Instant createdOn;

    @Enumerated(EnumType.STRING)
    private Sentiment sentiment;

    public static List<Review> findByMovieId(UUID movieId) {
        return find("SELECT r FROM Review r WHERE r.movie.id = ?1", movieId).list();
    }
}
