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
import sit.tu_varna.bg.api.operation.genre.add.AddGenreRequest;
import sit.tu_varna.bg.api.operation.genre.edit.EditGenreRequest;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.entity.Genre;

import java.util.Set;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNull.notNullValue;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@QuarkusTestResource(OidcWiremockTestResource.class)
class GenreResourceTest {

    private static final String BASE_URL = "/api/genres";
    private static final String ADMIN_ROLE = BusinessConstants.ADMIN_ROLE;

    private UUID genreId;

    @BeforeEach
    @Transactional
    void setup() {
        clearDatabase();

        // Setup Genre
        Genre genre = new Genre();
        genre.setName("Action");
        genre.persist();

        genreId = genre.getId();
    }

    @Transactional
    void clearDatabase() {
        Genre.deleteAll();
    }

    @Test
    public void testGetAllGenres() {
        given()
                .when().get(BASE_URL)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("genres", hasSize(1));
    }

    @Test
    public void testAddGenre() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        AddGenreRequest addGenreRequest = AddGenreRequest.builder()
                .name("New Genre")
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(addGenreRequest)
                .when().post(BASE_URL)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("genreId", notNullValue());
    }

    @Test
    public void testEditGenre() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        EditGenreRequest editGenreRequest = EditGenreRequest.builder()
                .name("Updated Genre")
                .genreId(genreId)
                .build();

        given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(editGenreRequest)
                .when().put(BASE_URL + "/{genreId}", genreId.toString())
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON);
    }

    @Test
    public void testDeleteGenre() {
        String accessToken = getAccessToken("admin", Set.of(ADMIN_ROLE));

        given()
                .auth().oauth2(accessToken)
                .pathParam("genreId", genreId.toString())
                .when().delete(BASE_URL + "/{genreId}")
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
