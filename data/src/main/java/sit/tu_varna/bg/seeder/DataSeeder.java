package sit.tu_varna.bg.seeder;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class DataSeeder {
    @ConfigProperty(name = "app.seed-data")
    boolean seedData;


    void onStart(@Observes StartupEvent ev) {
        if (seedData) {
            seedData();
        }
    }

    @Transactional
    void seedData() {
        MovieSeeder.seedMovies();
    }
}
