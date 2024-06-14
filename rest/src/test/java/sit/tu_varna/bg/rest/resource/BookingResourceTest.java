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
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingRequest;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.entity.*;
import sit.tu_varna.bg.enums.BookingStatus;
import sit.tu_varna.bg.enums.ShowtimeType;
import sit.tu_varna.bg.enums.TicketStatus;
import sit.tu_varna.bg.enums.TicketType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class BookingResourceTest {

    private static final String BASE_URL = "/api/bookings";
    private static final String ADMIN_ROLE = BusinessConstants.ADMIN_ROLE;
    private static final String OPERATOR_ROLE = BusinessConstants.OPERATOR_ROLE;

    private UUID showtimeId;
    private UUID bookingId;
    private UUID cinemaId;

    private static int shortCodeCounter = 11111;

    @BeforeEach
    @Transactional
    void setup() {
        // Setup User
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.persist();

        Cinema cinema = new Cinema();
        cinema.persist();

        Hall hall = new Hall();
        hall.persist();

        Movie movie = new Movie();
        movie.persist();

        // Setup Showtime
        Showtime showtime = new Showtime();
        showtime.setCinema(cinema);
        showtime.setHall(hall);
        showtime.setMovie(movie);
        showtime.setType(ShowtimeType.TWO_D);
        showtime.setStartTime(LocalDateTime.now().plusHours(1));
        showtime.setTicketPrice(BigDecimal.TEN);
        showtime.persist();

        // Setup Booking with unique shortCode
        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShortCode(generateUniqueShortCode());
        booking.setShowtime(showtime);
        booking.setStatus(BookingStatus.AVAILABLE);
        booking.persist();

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
        ticket.setBooking(booking);
        ticket.setUser(user);
        ticket.setShowtime(showtime);
        ticket.setShowtimeSeat(showtimeSeat);
        ticket.setTicketType(TicketType.NORMAL);
        ticket.setPrice(BigDecimal.TEN);
        ticket.setTicketStatus(TicketStatus.BOOKED);
        ticket.persist();

        showtimeId = showtime.getId();
        bookingId = booking.getId();
        cinemaId = cinema.getId();
    }

    @Test
    public void testGetShowtimeBookings() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("showtimeId", showtimeId.toString())
                .when().get(BASE_URL + "/showtimes/{showtimeId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("bookings", hasSize(1));
    }

    @Test
    public void testCancelBooking() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        CancelBookingRequest cancelBookingRequest = CancelBookingRequest.builder()
                .bookingId(bookingId)
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(cancelBookingRequest)
                .when().put(BASE_URL + "/" + bookingId)
                .then()
                .statusCode(200)
                .body("cancelled", is(true));
    }

    @Test
    public void testValidateBooking() {
        String accessToken = getAccessToken("operator", Set.of(OPERATOR_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("bookingShortCode", "11113")
                .queryParam("cinema", cinemaId.toString())
                .when().get(BASE_URL + "/validate/{bookingShortCode}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("booking.id", equalTo(bookingId.toString()));
    }

    @Test
    public void testValidateBookingNotFound() {
        String accessToken = getAccessToken("operator", Set.of(OPERATOR_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("bookingShortCode", "00000")
                .queryParam("cinema", cinemaId.toString())
                .when().get(BASE_URL + "/validate/{bookingShortCode}")
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

    private String generateUniqueShortCode() {
        String shortCode;
        do {
            shortCode = String.valueOf(shortCodeCounter++);
        } while (Booking.find("shortCode", shortCode).count() > 0);
        return shortCode;
    }

}
