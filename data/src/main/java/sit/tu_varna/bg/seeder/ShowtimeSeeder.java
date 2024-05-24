package sit.tu_varna.bg.seeder;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import sit.tu_varna.bg.entity.Showtime;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@ApplicationScoped
public class ShowtimeSeeder {
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
            showtime.setStartTime(generateRandomDateTime());
            showtime.persist();
        }
    }

    // in the next 7 days
    private LocalDateTime generateRandomDateTime() {
        LocalDateTime now = LocalDateTime.now();
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
