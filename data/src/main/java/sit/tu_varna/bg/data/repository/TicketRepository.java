package sit.tu_varna.bg.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sit.tu_varna.bg.data.entity.Ticket;

import java.util.UUID;

public interface TicketRepository extends JpaRepository<Ticket, UUID> {
}
