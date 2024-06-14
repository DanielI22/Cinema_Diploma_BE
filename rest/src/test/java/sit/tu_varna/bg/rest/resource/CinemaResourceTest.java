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
import sit.tu_varna.bg.api.operation.cinema.add.AddCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.edit.EditCinemaRequest;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.enums.ShowtimeType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class CinemaResourceTest {

    private static final String BASE_URL = "/api/cinemas";
    private static final String ADMIN_ROLE = BusinessConstants.ADMIN_ROLE;
    private static final String PROJECTOR_ROLE = BusinessConstants.PROJECTOR_ROLE;

    private UUID cinemaId;

    @BeforeEach
    @Transactional
    void setup() {
        // Setup Cinema
        Cinema cinema = new Cinema();
        cinema.persist();

        Hall hall = new Hall();
        hall.setCinema(cinema);
        hall.persist();

        Movie movie = new Movie();
        movie.persist();

        Showtime showtime = new Showtime();
        showtime.setCinema(cinema);
        showtime.setHall(hall);
        showtime.setMovie(movie);
        showtime.setStartTime(LocalDate.now().plusDays(1).atStartOfDay());
        showtime.setType(ShowtimeType.TWO_D);
        showtime.setTicketPrice(BigDecimal.TEN);
        showtime.persist();

        cinemaId = cinema.getId();
    }

    @Test
    public void testGetAllCinemas() {
        given()
                .when().get(BASE_URL)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("cinemas", notNullValue());
    }

    @Test
    public void testGetCinema() {
        given()
                .pathParam("cinemaId", cinemaId.toString())
                .when().get(BASE_URL + "/{cinemaId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("cinema.id", equalTo(cinemaId.toString()));
    }

    @Test
    public void testAddCinema() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        AddCinemaRequest addCinemaRequest = AddCinemaRequest.builder()
                .name("New Cinema")
                .location("New Location")
                .halls(Collections.emptyList())
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(addCinemaRequest)
                .when().post(BASE_URL)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("cinemaId", notNullValue());
    }

    @Test
    public void testEditCinema() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        EditCinemaRequest editCinemaRequest = EditCinemaRequest.builder()
                .name("Updated Cinema")
                .location("Updated Location")
                .cinemaId(cinemaId)
                .halls(Collections.emptyList())
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(editCinemaRequest)
                .when().put(BASE_URL + "/{cinemaId}", cinemaId.toString())
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testDeleteCinema() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("cinemaId", cinemaId.toString())
                .when().delete(BASE_URL + "/{cinemaId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("deleted", is(true));
    }

    @Test
    public void testGetCinemaHalls() {
        String accessToken = getAccessToken("projector", Set.of(PROJECTOR_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("cinemaId", cinemaId.toString())
                .when().get(BASE_URL + "/{cinemaId}/halls")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("halls", hasSize(1));
    }

    @Test
    public void testGetCinemaShowtimesByDate() {
        given()
                .pathParam("cinemaId", cinemaId.toString())
                .queryParam("date", LocalDate.now().plusDays(1).toString())
                .when().get(BASE_URL + "/{cinemaId}/showtimes")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("showtimes", hasSize(1));
    }

    @Test
    public void testGetCinemaShowtimesByInvalidDate() {
        given()
                .pathParam("cinemaId", cinemaId.toString())
                .queryParam("date", "invalid-date")
                .when().get(BASE_URL + "/{cinemaId}/showtimes")
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