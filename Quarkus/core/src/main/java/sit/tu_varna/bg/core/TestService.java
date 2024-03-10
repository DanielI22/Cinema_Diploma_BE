package sit.tu_varna.bg.core;

import sit.tu_varna.bg.entity.Movie;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

@ApplicationScoped
public class TestService {

    @Transactional
    public void test() {
//        Genre test = Genre.builder().name("test").build();
//        test.persist();
//        Movie theAvengers = Movie.builder()
//                .title("The Avengers")
//                .releaseYear(2012)
//                .description("Earth's mightiest heroes must come together...")
//                .posterImageUrl("https://example.com/posters/the-avengers.jpg")
//                .trailerUrl("https://example.com/trailers/the-avengers.mp4")
//                .genres(new HashSet<>(List.of(test)))
//                .build();
        Movie movie = (Movie) Movie.findAll().stream().findFirst().get();
        System.out.println(movie);
        if(movie.isPersistent()){
            // delete it
            movie.delete();
        }

//        Movie movie = Movie.findById(theAvengers.getId());
//        movie.delete();
//        Movie movie2 = Movie.findById(theAvengers.getId());
//        boolean istrue = movie2.isPersistent();
    }
}
