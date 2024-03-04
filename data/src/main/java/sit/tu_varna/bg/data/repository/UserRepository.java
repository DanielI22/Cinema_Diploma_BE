package sit.tu_varna.bg.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.tu_varna.bg.data.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
}
