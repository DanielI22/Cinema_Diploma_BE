package sit.tu_varna.bg.seeder;

import sit.tu_varna.bg.entity.Genre;
import sit.tu_varna.bg.entity.Movie;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class MovieSeeder {
    public static void seedMovies() {
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

        Genre.persist(Arrays.asList(action, fantasy, drama, comedy, thriller));

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

        Movie.persist(Arrays.asList(theAvengers, jumanji, inception, interstellar, theGodfather));
    }
}
