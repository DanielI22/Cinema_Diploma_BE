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
import sit.tu_varna.bg.api.operation.hall.add.AddHallRequest;
import sit.tu_varna.bg.api.operation.hall.edit.EditHallRequest;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.entity.Cinema;
import sit.tu_varna.bg.entity.Hall;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.Showtime;
import sit.tu_varna.bg.enums.ShowtimeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class HallResourceTest {

    private static final String BASE_URL = "/api/halls";
    private static final String ADMIN_ROLE = BusinessConstants.ADMIN_ROLE;

    private UUID hallId;
    private UUID showtimeId;

    @BeforeEach
    @Transactional
    void setup() {
        Cinema cinema = new Cinema();
        cinema.persist();

        Hall hall = new Hall();
        hall.setCinema(cinema);
        hall.persist();

        Movie movie = new Movie();
        movie.persist();

        // Setup Showtime
        Showtime showtime = new Showtime();
        showtime.setCinema(cinema);
        showtime.setHall(hall);
        showtime.setMovie(movie);
        showtime.setStartTime(LocalDateTime.now().plusDays(1));
        showtime.setType(ShowtimeType.TWO_D);
        showtime.setTicketPrice(BigDecimal.TEN);
        showtime.persist();

        hallId = hall.getId();
        showtimeId = showtime.getId();
    }

    @Test
    public void testGetAllHalls() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .when().get(BASE_URL)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("halls", notNullValue());
    }

    @Test
    public void testGetAvailableHalls() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .when().get(BASE_URL + "/available")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("halls", notNullValue());
    }

    @Test
    public void testGetHall() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("hallId", hallId.toString())
                .when().get(BASE_URL + "/{hallId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("hall.id", equalTo(hallId.toString()));
    }

    @Test
    public void testGetShowtimeHall() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("showtimeId", showtimeId.toString())
                .when().get(BASE_URL + "/showtimes/{showtimeId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("hall.id", equalTo(hallId.toString()));
    }

    @Test
    public void testAddHall() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        AddHallRequest addHallRequest = AddHallRequest.builder()
                .name("New Hall")
                .rows(Collections.emptyList())
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(addHallRequest)
                .when().post(BASE_URL)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("hallId", notNullValue());
    }

    @Test
    public void testEditHall() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        EditHallRequest editHallRequest = EditHallRequest.builder()
                .name("Updated Hall")
                .hallId(hallId)
                .rows(Collections.emptyList())
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(editHallRequest)
                .when().put(BASE_URL + "/{hallId}", hallId.toString())
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testDeleteHall() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("hallId", hallId.toString())
                .when().delete(BASE_URL + "/{hallId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("deleted", is(true));
    }

    private String getAccessToken(String userName, Set<String> groups) {
        return Jwt.preferredUserName(userName)
                .groups(groups)
                .issuer("https://server.example.com")
                .audience("https://service.example.com")
                .sign();
    }
}
