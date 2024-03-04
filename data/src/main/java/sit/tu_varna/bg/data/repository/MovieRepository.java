package sit.tu_varna.bg.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.tu_varna.bg.data.entity.Movie;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
}
