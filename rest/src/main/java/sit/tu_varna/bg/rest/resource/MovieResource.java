package sit.tu_varna.bg.rest.resource;

import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesOperation;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesRequest;
import sit.tu_varna.bg.api.operation.movie.getbygenre.GetMovieByGenreOperation;
import sit.tu_varna.bg.api.operation.movie.getbygenre.GetMoviesByGenreRequest;
import sit.tu_varna.bg.api.operation.movie.getbyid.GetMovieOperation;
import sit.tu_varna.bg.api.operation.movie.getbyid.GetMovieRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

@Path("/api/movies")
public class MovieResource {
    private final GetAllMoviesOperation getAllMoviesOperation;
    private final GetMovieByGenreOperation getMovieByGenreOperation;
    private final GetMovieOperation getMovieOperation;

    public MovieResource(GetAllMoviesOperation getAllMoviesOperation, GetMovieByGenreOperation getMovieByGenreOperation, GetMovieOperation getMovieOperation) {
        this.getAllMoviesOperation = getAllMoviesOperation;
        this.getMovieByGenreOperation = getMovieByGenreOperation;
        this.getMovieOperation = getMovieOperation;
    }

    @GET
    public Response getAllMovies() {
        return Response.ok(getAllMoviesOperation.process(new GetAllMoviesRequest())).build();
    }

    @GET
    @Path("/{movieId}")
    public Response getMovie(@PathParam("movieId")
                                 @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                         message = "Invalid UUID format")
                                         String movieId) {
        GetMovieRequest request = GetMovieRequest
                .builder()
                .movieId(UUID.fromString(movieId))
                .build();
        return Response.ok(getMovieOperation.process(request)).build();
    }

    @GET
    @Path("/genre/{genreName}")
    public Response getMoviesByGenre(@PathParam("genreName") String genreName) {
        GetMoviesByGenreRequest request = GetMoviesByGenreRequest
                .builder()
                .genreName(genreName)
                .build();
        return Response.ok(getMovieByGenreOperation.process(request)).build();
    }


//    @GET
//    @Path("/user")
//    @RolesAllowed({"client", "admin"})
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getUserName() {
//        Principal principal = securityIdentity.getPrincipal();
//        return principal.getName();
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @PermitAll
//    public Response test() {
//        return Response.ok("test").build();
//    }
//
//    @GET
//    @Path("client")
//    @RolesAllowed({"client", "admin"})
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response testClient() {
//        return Response.ok("test").build();
//    }
//
//    @GET
//    @Path("operator")
//    @RolesAllowed("operator")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response testOperator() {
//        return Response.ok("test").build();
//    }
//
//    @GET
//    @Path("admin")
//    @RolesAllowed("admin")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response testAdmin() {
//        return Response.ok("test").build();
//    }
}
