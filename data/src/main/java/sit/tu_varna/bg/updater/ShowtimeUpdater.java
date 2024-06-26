package sit.tu_varna.bg.updater;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sit.tu_varna.bg.entity.Showtime;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class ShowtimeUpdater {
    @ConfigProperty(name = "app.update-showtimes")
    boolean updateShowtimes;


    void onStart(@Observes StartupEvent ev) {
        if (updateShowtimes) {
            updateShowtimes();
        }
    }

    @Transactional
    void updateShowtimes() {
        List<Showtime> showtimes = Showtime.findAll().list();

        for (Showtime showtime : showtimes) {
            LocalDateTime newStartTime;
            do {
                newStartTime = generateRandomDateTime();
            } while (isOverlapping(showtime.getHall().getId(), newStartTime, showtime.getMovie().getDuration()));

            showtime.setStartTime(newStartTime);
            showtime.persist();
        }
    }

    private boolean isOverlapping(UUID hallId, LocalDateTime newStartTime, int duration) {
        LocalDateTime newEndTime = newStartTime.plusMinutes(duration);

        List<Showtime> existingShowtimes = Showtime.find("hall.id", hallId).list();
        for (Showtime existingShowtime : existingShowtimes) {
            LocalDateTime existingStartTime = existingShowtime.getStartTime();
            LocalDateTime existingEndTime = existingStartTime.plusMinutes(existingShowtime.getMovie().getDuration());

            // Check for overlap
            if ((newStartTime.isBefore(existingEndTime) && newEndTime.isAfter(existingStartTime))) {
                return true;
            }
        }
        return false;
    }

    // Generate a random date-time within the next 7 days, adjusted to the nearest 5-minute mark
    private LocalDateTime generateRandomDateTime() {
        LocalDateTime now = LocalDate.now().atStartOfDay();
        LocalDateTime end = now.plusDays(7);

        // Convert LocalDateTime to epoch seconds
        long nowEpochSecond = now.toEpochSecond(ZoneOffset.UTC);
        long endEpochSecond = end.toEpochSecond(ZoneOffset.UTC);

        // Generate a random epoch second between now and the end date
        long randomEpochSecond = ThreadLocalRandom.current().nextLong(nowEpochSecond, endEpochSecond);

        // Adjust to the nearest 5-minute mark
        long randomEpochMinute = randomEpochSecond / 60;
        long adjustedEpochMinute = randomEpochMinute - (randomEpochMinute % 5);

        // Convert epoch minutes back to LocalDateTime
        return LocalDateTime.ofEpochSecond(adjustedEpochMinute * 60, 0, ZoneOffset.UTC);
    }

}
