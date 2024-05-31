package sit.tu_varna.bg.core.scheduler;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.entity.ShowtimeSeat;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.enums.BookingStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ShowtimeSchedulerService {

    @Scheduled(every = "5m")
    @Transactional
    public void checkShowtimes() {
        System.out.println("Showtime expire check");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime threshold = now.plusMinutes(15);

        List<Showtime> showtimesStartingSoon = Showtime.findStartingSoon(now, threshold);

        for (Showtime showtime : showtimesStartingSoon) {
            List<Booking> bookings = Booking.findByShowtimeId(showtime.getId());
            for (Booking booking : bookings) {
                if (booking.getStatus().equals(BookingStatus.AVAILABLE)) {
                    booking.setStatus(BookingStatus.EXPIRED);
                }
                List<ShowtimeSeat> showtimeSeats = booking.getTickets()
                        .stream()
                        .map(Ticket::getShowtimeSeat)
                        .collect(Collectors.toList());
                for (ShowtimeSeat showtimeSeat : showtimeSeats) {
                    showtimeSeat.setBooked(false);
                    showtimeSeat.persist();
                }
                booking.persist();
            }
        }
    }
}
