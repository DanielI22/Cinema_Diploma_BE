package sit.tu_varna.bg.rest.resource;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.oidc.server.OidcWiremockTestResource;
import io.restassured.http.ContentType;
import io.smallrye.jwt.build.Jwt;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.api.dto.GenreDto;
import sit.tu_varna.bg.api.operation.movie.add.AddMovieRequest;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.entity.Genre;
import sit.tu_varna.bg.entity.Movie;

import java.util.List;
import java.util.Set;

import static io.restassured.RestAssured.given;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class MovieResourceTest {

    private static final String BASE_URL = "/api/movies";
    private static final String ADMIN_ROLE = BusinessConstants.ADMIN_ROLE;

    private String movieId;

    @BeforeEach
    @Transactional
    void setup() {
        Genre genre = new Genre();
        genre.setName("Drama");
        genre.persist();

        Movie movie = new Movie();
        movie.setTitle("Test Movie");
        movie.setDescription("A test movie");
        movie.setReleaseYear(2024);
        movie.setDuration(120);
        movie.setTrailerUrl("http://example.com/trailer");
        movie.setPosterImageUrl("http://example.com/image");
        movie.setGenres(Set.of(genre));
        movie.persist();

        // Fetch the ID of the inserted movie
        movieId = movie.getId().toString();
    }

    @Test
    public void testGetMovies() {
        given()
                .when().get(BASE_URL)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testAddMovie() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));
        AddMovieRequest newMovie = AddMovieRequest.builder()
                .title("Test Movie")
                .description("A test movie")
                .releaseYear(2024)
                .duration(120)
                .trailerUrl("http://example.com/trailer")
                .imageUrl("http://example.com/image")
                .genres(List.of(GenreDto.builder().name("Drama").build()))
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(newMovie)
                .when().post(BASE_URL)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testAddMovieInvalidData() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));
        AddMovieRequest invalidMovie = AddMovieRequest.builder()
                .title("")
                .releaseYear(-2024)
                .duration(-120)
                .genres(null)
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(invalidMovie)
                .when().post(BASE_URL)
                .then()
                .statusCode(400)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testDeleteMovie() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("movieId", movieId)
                .when().delete(BASE_URL + "/{movieId}")
                .then()
                .statusCode(200);
    }

    @Test
    public void testDeleteMovieInvalidUUIDFormat() {
        String movieId = "invalidId";
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("movieId", movieId)
                .when().delete(BASE_URL + "/{movieId}")
                .then()
                .statusCode(400);
    }

    @Test
    public void testDeleteMovieNotFound() {
        String nonExistentMovieId = "9e0dba11-3f58-49ce-8053-4361849009fd";
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("movieId", nonExistentMovieId)
                .when().delete(BASE_URL + "/{movieId}")
                .then()
                .statusCode(404);
    }

    private String getAccessToken(String userName, Set<String> groups) {
        return Jwt.preferredUserName(userName)
                .groups(groups)
                .issuer("https://server.example.com")
                .audience("https://service.example.com")
                .sign();
    }
}
