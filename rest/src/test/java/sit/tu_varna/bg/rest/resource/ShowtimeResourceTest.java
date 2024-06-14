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
import sit.tu_varna.bg.api.operation.showtime.add.AddShowtimeRequest;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.enums.ShowtimeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class ShowtimeResourceTest {

    private static final String BASE_URL = "/api/showtimes";
    private static final String ADMIN_ROLE = BusinessConstants.ADMIN_ROLE;
    private static final String PROJECTOR_ROLE = BusinessConstants.PROJECTOR_ROLE;

    private UUID showtimeId;
    private UUID hallId;
    private UUID movieId;

    @BeforeEach
    @Transactional
    void setup() {
        // Setup Hall
        Hall hall = new Hall();
        hall.persist();
        hallId = hall.getId();

        // Setup Movie
        Movie movie = new Movie();
        movie.persist();
        movieId = movie.getId();

        Cinema cinema = new Cinema();
        cinema.persist();

        // Setup Showtime
        Showtime showtime = new Showtime();
        showtime.setHall(hall);
        showtime.setMovie(movie);
        showtime.setStartTime(LocalDate.now().plusDays(1).atStartOfDay());
        showtime.setType(ShowtimeType.TWO_D);
        showtime.setTicketPrice(BigDecimal.TEN);
        showtime.setCinema(cinema);
        showtime.persist();
        showtimeId = showtime.getId();
    }

    @Test
    public void testDeleteShowtime() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("showtimeId", showtimeId.toString())
                .when().delete(BASE_URL + "/{showtimeId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("deleted", is(true));
    }

    @Test
    public void testSetUpcomingShowtime() {
        String accessToken = getAccessToken("projector", Set.of(PROJECTOR_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("showtimeId", showtimeId.toString())
                .when().put(BASE_URL + "/{showtimeId}/upcoming")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("upcoming", is(true));
    }

    @Test
    public void testGetShowtimeInvalidUUID() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("showtimeId", "invalid-uuid")
                .when().get(BASE_URL + "/{showtimeId}")
                .then()
                .statusCode(400);
    }

    @Test
    public void testAddShowtimeInvalidData() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        AddShowtimeRequest addShowtimeRequest = AddShowtimeRequest.builder()
                .hallId(hallId)
                .movieId(movieId)
                .startingTime(null) // Invalid start time
                .type("3D")
                .ticketPrice(BigDecimal.valueOf(15))
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(addShowtimeRequest)
                .when().post(BASE_URL)
                .then()
                .statusCode(400);
    }

    private String getAccessToken(String userName, Set<String> groups) {
        return Jwt.preferredUserName(userName)
                .groups(groups)
                .issuer("https://server.example.com")
                .audience("https://service.example.com")
                .sign();
    }
}
