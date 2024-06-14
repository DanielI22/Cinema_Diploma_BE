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
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.entity.*;
import sit.tu_varna.bg.enums.ShowtimeType;
import sit.tu_varna.bg.enums.TicketStatus;
import sit.tu_varna.bg.enums.TicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class TicketResourceTest {

    private static final String BASE_URL = "/api/tickets";
    private static final String ADMIN_ROLE = BusinessConstants.ADMIN_ROLE;
    private static final String OPERATOR_ROLE = BusinessConstants.OPERATOR_ROLE;
    private static final String VALIDATOR_ROLE = BusinessConstants.VALIDATOR_ROLE;

    private UUID ticketId;
    private UUID showtimeId;
    private UUID cinemaId;
    private static int shortCodeCounter = 11111;

    @BeforeEach
    @Transactional
    void setup() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.persist();

        // Setup Cinema
        Cinema cinema = new Cinema();
        cinema.persist();
        cinemaId = cinema.getId();

        // Setup Hall
        Hall hall = new Hall();
        hall.setCinema(cinema);
        hall.persist();

        // Setup Movie
        Movie movie = new Movie();
        movie.persist();

        // Setup Showtime
        Showtime showtime = new Showtime();
        showtime.setHall(hall);
        showtime.setMovie(movie);
        showtime.setStartTime(LocalDateTime.now().plusHours(1));
        showtime.setType(ShowtimeType.TWO_D);
        showtime.setTicketPrice(BigDecimal.TEN);
        showtime.setCinema(cinema);
        showtime.persist();
        showtimeId = showtime.getId();

        Row row = new Row();
        row.setRowNumber(1);
        row.persist();

        // Setup ShowtimeSeat
        Seat seat = new Seat();
        seat.setSeatNumber(1);
        seat.setRow(row);
        seat.persist();

        ShowtimeSeat showtimeSeat = new ShowtimeSeat();
        showtimeSeat.setSeat(seat);
        showtimeSeat.setShowtime(showtime);
        showtimeSeat.setBooked(false);
        showtimeSeat.persist();

        // Setup Ticket
        Ticket ticket = new Ticket();
        ticket.setBooking(null);
        ticket.setUser(user);
        ticket.setShortCode(generateUniqueShortCode());
        ticket.setShowtime(showtime);
        ticket.setTicketType(TicketType.NORMAL);
        ticket.setPrice(BigDecimal.TEN);
        ticket.setTicketStatus(TicketStatus.PURCHASED);
        ticket.setShowtimeSeat(showtimeSeat);
        ticket.persist();
        ticketId = ticket.getId();
    }

    @Test
    public void testGetTicket() {
        String accessToken = getAccessToken("operator", Set.of(OPERATOR_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("ticketId", ticketId.toString())
                .when().get(BASE_URL + "/{ticketId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("ticket.id", equalTo(ticketId.toString()));
    }

    @Test
    public void testGetShowtimeTickets() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("showtimeId", showtimeId.toString())
                .when().get(BASE_URL + "/showtimes/{showtimeId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("tickets", hasSize(1));
    }

    @Test
    public void testValidateTicketNotFound() {
        String accessToken = getAccessToken("validator", Set.of(VALIDATOR_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("ticketShortCode", "00000")
                .queryParam("cinema", cinemaId.toString())
                .when().get(BASE_URL + "/validate/{ticketShortCode}")
                .then()
                .statusCode(404);
    }

    @Test
    public void testGetTicketInvalidUUID() {
        String accessToken = getAccessToken("operator", Set.of(OPERATOR_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("ticketId", "invalid-uuid")
                .when().get(BASE_URL + "/{ticketId}")
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

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = String.valueOf(shortCodeCounter++);
        } while (Ticket.find("shortCode", shortCode).count() > 0);
        return shortCode;
    }
}
