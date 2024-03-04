package sit.tu_varna.bg.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.tu_varna.bg.data.entity.Booking;

import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {
}
