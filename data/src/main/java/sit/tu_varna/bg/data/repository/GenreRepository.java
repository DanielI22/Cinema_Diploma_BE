package sit.tu_varna.bg.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.tu_varna.bg.data.entity.Genre;

import java.util.UUID;

public interface GenreRepository extends JpaRepository<Genre, UUID> {
}
