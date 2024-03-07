package sit.tu_varna.bg.data.seeder;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import sit.tu_varna.bg.data.entity.Genre;
import sit.tu_varna.bg.data.entity.Movie;
import sit.tu_varna.bg.data.repository.GenreRepository;
import sit.tu_varna.bg.data.repository.MovieRepository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MovieSeeder implements CommandLineRunner {
    @Value("${app.db.seedOnStartup}")
    private boolean seedOnStartup;
    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;

    @Override
    @Transactional
    public void run(String... args) {
        if (seedOnStartup) {
            Genre action = Genre
                    .builder()
                    .name("action")
                    .build();
            Genre fantasy = Genre
                    .builder()
                    .name("fantasy")
                    .build();
            Genre drama = Genre
                    .builder()
                    .name("drama")
                    .build();
            Genre comedy = Genre
                    .builder()
                    .name("comedy")
                    .build();
            Genre thriller = Genre
                    .builder()
                    .name("thriller")
                    .build();

            genreRepository.saveAll(Arrays.asList(action, fantasy, drama, comedy, thriller));

            Movie theAvengers = Movie.builder()
                    .title("The Avengers")
                    .releaseYear(2012)
                    .description("Earth's mightiest heroes must come together...")
                    .posterImageUrl("https://example.com/posters/the-avengers.jpg")
                    .trailerUrl("https://example.com/trailers/the-avengers.mp4")
                    .genres(new HashSet<>(List.of(drama, comedy)))
                    .build();

            Movie jumanji = Movie.builder()
                    .title("Jumanji: Welcome to the Jungle")
                    .releaseYear(2017)
                    .description("Four teenagers are sucked into a magical video game...")
                    .posterImageUrl("https://example.com/posters/jumanji.jpg")
                    .trailerUrl("https://example.com/trailers/jumanji.mp4")
                    .genres(new HashSet<>(List.of(fantasy)))
                    .build();

            Movie inception = Movie.builder()
                    .title("Inception")
                    .releaseYear(2010)
                    .description("A thief who steals corporate secrets through use of dream-sharing technology...")
                    .posterImageUrl("https://example.com/posters/inception.jpg")
                    .trailerUrl("https://example.com/trailers/inception.mp4")
                    .genres(new HashSet<>(List.of(thriller)))
                    .build();

            Movie interstellar = Movie.builder()
                    .title("Interstellar")
                    .releaseYear(2014)
                    .description("A team of explorers travel through a wormhole in space...")
                    .posterImageUrl("https://example.com/posters/interstellar.jpg")
                    .trailerUrl("https://example.com/trailers/interstellar.mp4")
                    .genres(new HashSet<>(List.of(action, fantasy)))
                    .build();

            Movie theGodfather = Movie.builder()
                    .title("The Godfather")
                    .releaseYear(1972)
                    .description("The aging patriarch of an organized crime dynasty transfers control...")
                    .posterImageUrl("https://example.com/posters/the-godfather.jpg")
                    .trailerUrl("https://example.com/trailers/the-godfather.mp4")
                    .genres(new HashSet<>(List.of(action)))
                    .build();

            movieRepository.saveAll(Arrays.asList(theAvengers, jumanji, inception, interstellar, theGodfather));
        }
    }
}
