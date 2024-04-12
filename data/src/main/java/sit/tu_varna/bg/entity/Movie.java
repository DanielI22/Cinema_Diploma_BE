package sit.tu_varna.bg.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE movies SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Movie extends PanacheEntityBase {
    @Id
    @GeneratedValue
    private UUID id;

    private String title;
    @Column(length = 1024)
    private String description;
    private int releaseYear;
    private String posterImageUrl;
    private String trailerUrl;
    private int duration;

    @OneToMany(mappedBy = "movie")
    private Set<Review> reviews = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "movie_genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();

    @CreationTimestamp
    private Instant createdOn;

    private boolean deleted = Boolean.FALSE;

    public static List<Movie> findByGenreName(String genreName) {
        return find("SELECT m FROM Movie m JOIN m.genres g WHERE g.name = ?1", genreName).list();
    }
}