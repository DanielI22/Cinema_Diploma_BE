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
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.Review;
import sit.tu_varna.bg.entity.User;

import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class ReviewResourceTest {

    private static final String BASE_URL = "/api/reviews";
    private static final String ADMIN_ROLE = BusinessConstants.ADMIN_ROLE;

    private UUID reviewId;

    @BeforeEach
    @Transactional
    void setup() {
        // Setup Movie
        Movie movie = new Movie();
        movie.persist();

        // Setup User
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@example.com");
        user.persist();

        // Setup Review
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setReviewText("Great movie!");
        review.persist();

        reviewId = review.getId();
    }

    @Test
    public void testDeleteReview() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("reviewId", reviewId.toString())
                .when().delete(BASE_URL + "/{reviewId}")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("deleted", is(true));
    }

    @Test
    public void testDeleteReviewInvalidUUIDFormat() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("reviewId", "invalidId")
                .when().delete(BASE_URL + "/{reviewId}")
                .then()
                .statusCode(400);
    }

    @Test
    public void testDeleteReviewNotFound() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("reviewId", UUID.randomUUID().toString())
                .when().delete(BASE_URL + "/{reviewId}")
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
