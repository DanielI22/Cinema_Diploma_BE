package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import sit.tu_varna.bg.api.operation.genre.add.AddGenreOperation;
import sit.tu_varna.bg.api.operation.genre.add.AddGenreRequest;
import sit.tu_varna.bg.api.operation.genre.delete.DeleteGenreOperation;
import sit.tu_varna.bg.api.operation.genre.delete.DeleteGenreRequest;
import sit.tu_varna.bg.api.operation.genre.edit.EditGenreOperation;
import sit.tu_varna.bg.api.operation.genre.edit.EditGenreRequest;
import sit.tu_varna.bg.api.operation.genre.getall.GetAllGenresOperation;
import sit.tu_varna.bg.api.operation.genre.getall.GetAllGenresRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

@Path("/api/genres")
public class GenreResource {
    @Inject
    GetAllGenresOperation getAllGenresOperation;
    @Inject
    AddGenreOperation addGenreOperation;
    @Inject
    EditGenreOperation editGenreOperation;
    @Inject
    DeleteGenreOperation deleteGenreOperation;

    @GET
    public Response getAllGenres() {
        return Response.ok(getAllGenresOperation.process(new GetAllGenresRequest())).build();
    }

    @POST
    public Response addGenre(@Valid AddGenreRequest addGenreRequest) {
        return Response.ok(addGenreOperation.process(addGenreRequest)).build();
    }

    @PUT
    @Path("/{genreId}")
    public Response editGenre(@PathParam("genreId")
                              @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                      message = "Invalid UUID format")
                                      String cinemaId, @Valid EditGenreRequest editCinemaRequest) {
        editCinemaRequest.setGenreId(UUID.fromString(cinemaId));
        return Response.ok(editGenreOperation.process(editCinemaRequest)).build();
    }

    @DELETE
    @Path("/{genreId}")
    public Response deleteGenre(@PathParam("genreId")
                                @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                        message = "Invalid UUID format")
                                        String cinemaId) {
        DeleteGenreRequest deleteGenreRequest = DeleteGenreRequest
                .builder()
                .genreId(UUID.fromString(cinemaId))
                .build();
        return Response.ok(deleteGenreOperation.process(deleteGenreRequest)).build();
    }
}
