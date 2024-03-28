package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import sit.tu_varna.bg.api.operation.cinema.add.AddCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.add.AddCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.delete.DeleteCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.delete.DeleteCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.edit.EditCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.edit.EditCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.getall.GetAllCinemasOperation;
import sit.tu_varna.bg.api.operation.cinema.getall.GetAllCinemasRequest;
import sit.tu_varna.bg.api.operation.cinema.getbyid.GetCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.getbyid.GetCinemaRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

@Path("/api/cinemas")
public class CinemaResource {
    @Inject
    GetAllCinemasOperation getAllCinemasOperation;
    @Inject
    GetCinemaOperation getCinemaOperation;
    @Inject
    AddCinemaOperation addCinemaOperation;
    @Inject
    EditCinemaOperation editCinemaOperation;
    @Inject
    DeleteCinemaOperation deleteCinemaOperation;

    @GET
    public Response getAllCinemas() {
        return Response.ok(getAllCinemasOperation.process(new GetAllCinemasRequest())).build();
    }

    @GET
    @Path("/{cinemaId}")
    public Response getCinema(@PathParam("cinemaId")
                              @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                      message = "Invalid UUID format")
                                      String cinemaId) {
        GetCinemaRequest request = GetCinemaRequest
                .builder()
                .cinemaId(UUID.fromString(cinemaId))
                .build();
        return Response.ok(getCinemaOperation.process(request)).build();
    }

    @POST
    public Response addCinema(@Valid AddCinemaRequest addCinemaRequest) {
        return Response.ok(addCinemaOperation.process(addCinemaRequest)).build();
    }

    @PUT
    @Path("/{cinemaId}")
    public Response editCinema(@PathParam("cinemaId")
                               @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                       message = "Invalid UUID format")
                                       String cinemaId, @Valid EditCinemaRequest editCinemaRequest) {
        editCinemaRequest.setCinemaId(UUID.fromString(cinemaId));
        return Response.ok(editCinemaOperation.process(editCinemaRequest)).build();
    }

    @DELETE
    @Path("/{cinemaId}")
    public Response deleteCinema(@PathParam("cinemaId")
                                 @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                         message = "Invalid UUID format")
                                         String cinemaId) {
        DeleteCinemaRequest deleteCinemaRequest = DeleteCinemaRequest
                .builder()
                .cinemaId(UUID.fromString(cinemaId))
                .build();
        return Response.ok(deleteCinemaOperation.process(deleteCinemaRequest)).build();
    }
}
