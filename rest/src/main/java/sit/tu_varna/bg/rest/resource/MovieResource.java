package sit.tu_varna.bg.rest.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import sit.tu_varna.bg.api.operation.movie.add.AddMovieOperation;
import sit.tu_varna.bg.api.operation.movie.add.AddMovieRequest;
import sit.tu_varna.bg.api.operation.movie.delete.DeleteMovieOperation;
import sit.tu_varna.bg.api.operation.movie.delete.DeleteMovieRequest;
import sit.tu_varna.bg.api.operation.movie.edit.EditMovieOperation;
import sit.tu_varna.bg.api.operation.movie.edit.EditMovieRequest;
import sit.tu_varna.bg.api.operation.movie.externalapi.SearchMoviesOperation;
import sit.tu_varna.bg.api.operation.movie.externalapi.SearchMoviesRequest;
import sit.tu_varna.bg.api.operation.movie.get.GetMovieOperation;
import sit.tu_varna.bg.api.operation.movie.get.GetMovieRequest;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesOperation;
import sit.tu_varna.bg.api.operation.movie.getall.GetAllMoviesRequest;
import sit.tu_varna.bg.api.operation.movie.getbygenre.GetMoviesByGenreOperation;
import sit.tu_varna.bg.api.operation.movie.getbygenre.GetMoviesByGenreRequest;
import sit.tu_varna.bg.api.operation.showtime.getmoviebydate.GetMovieShowtimesByDateOperation;
import sit.tu_varna.bg.api.operation.showtime.getmoviebydate.GetMovieShowtimesByDateRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.time.LocalDate;
import java.util.UUID;

import static sit.tu_varna.bg.core.constants.BusinessConstants.ADMIN_ROLE;

@Path("/api/movies")
@Authenticated
public class MovieResource {
    @Inject
    GetAllMoviesOperation getAllMoviesOperation;
    @Inject
    GetMovieOperation getMovieOperation;
    @Inject
    AddMovieOperation addMovieOperation;
    @Inject
    EditMovieOperation editMovieOperation;
    @Inject
    DeleteMovieOperation deleteMovieOperation;
    @Inject
    GetMoviesByGenreOperation getMoviesByGenreOperation;
    @Inject
    SearchMoviesOperation searchMoviesOperation;
    @Inject
    GetMovieShowtimesByDateOperation getMovieShowtimesByDateOperation;

    @GET
    @PermitAll
    public Response getAllMovies() {
        return Response.ok(getAllMoviesOperation.process(new GetAllMoviesRequest())).build();
    }

    @GET
    @PermitAll
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
    @PermitAll
    @Path("/{movieId}/showtimes")
    public Response getMovieShowtimesByDate(@PathParam("movieId")
                                            @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                                    message = "Invalid UUID format")
                                                    String movieId,
                                            @QueryParam("date")
                                            @Pattern(regexp = ValidationConstants.LOCAL_DATE_REGEX,
                                                    message = "Invalid Local Date format")
                                                    String dateStr) {
        GetMovieShowtimesByDateRequest request = GetMovieShowtimesByDateRequest
                .builder()
                .movieId(UUID.fromString(movieId))
                .showtimeDate(LocalDate.parse(dateStr))
                .build();
        return Response.ok(getMovieShowtimesByDateOperation.process(request)).build();
    }

    @POST
    @RolesAllowed(ADMIN_ROLE)
    public Response addMovie(@Valid AddMovieRequest addMovieRequest) {
        return Response.ok(addMovieOperation.process(addMovieRequest)).build();
    }

    @PUT
    @RolesAllowed(ADMIN_ROLE)
    @Path("/{movieId}")
    public Response editMovie(@PathParam("movieId")
                              @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                      message = "Invalid UUID format")
                                      String movieId, @Valid EditMovieRequest editMovieRequest) {
        editMovieRequest.setMovieId(UUID.fromString(movieId));
        return Response.ok(editMovieOperation.process(editMovieRequest)).build();
    }

    @DELETE
    @RolesAllowed(ADMIN_ROLE)
    @Path("/{movieId}")
    public Response deleteMovie(@PathParam("movieId")
                                @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                        message = "Invalid UUID format")
                                        String movieId) {
        DeleteMovieRequest deleteMovieRequest = DeleteMovieRequest
                .builder()
                .movieId(UUID.fromString(movieId))
                .build();
        return Response.ok(deleteMovieOperation.process(deleteMovieRequest)).build();
    }

    @GET
    @RolesAllowed(ADMIN_ROLE)
    @Path("/search/{query}")
    public Response getMoviesApi(@PathParam("query") String query) {
        SearchMoviesRequest request = SearchMoviesRequest
                .builder()
                .query(query)
                .build();
        return Response.ok(searchMoviesOperation.process(request)).build();
    }

    @GET
    @PermitAll
    @Path("/genre/{genreName}")
    public Response getMoviesByGenre(@PathParam("genreName") String genreName) {
        GetMoviesByGenreRequest request = GetMoviesByGenreRequest
                .builder()
                .genreName(genreName)
                .build();
        return Response.ok(getMoviesByGenreOperation.process(request)).build();
    }
}
