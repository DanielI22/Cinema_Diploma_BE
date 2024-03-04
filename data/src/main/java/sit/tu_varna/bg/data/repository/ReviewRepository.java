package sit.tu_varna.bg.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.tu_varna.bg.data.entity.Review;

import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID> {
}
