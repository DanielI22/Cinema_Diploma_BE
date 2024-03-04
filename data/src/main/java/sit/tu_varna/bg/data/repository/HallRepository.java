package sit.tu_varna.bg.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.tu_varna.bg.data.entity.Hall;

import java.util.UUID;

public interface HallRepository extends JpaRepository<Hall, UUID> {
}
