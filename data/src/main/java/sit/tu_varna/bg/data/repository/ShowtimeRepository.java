package sit.tu_varna.bg.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.tu_varna.bg.data.entity.Showtime;

import java.util.UUID;

public interface ShowtimeRepository extends JpaRepository<Showtime, UUID> {
}
