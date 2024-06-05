package sit.tu_varna.bg.rest.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
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
import sit.tu_varna.bg.api.operation.cinema.get.GetCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.get.GetCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.getall.GetAllCinemasOperation;
import sit.tu_varna.bg.api.operation.cinema.getall.GetAllCinemasRequest;
import sit.tu_varna.bg.api.operation.cinema.gethalls.GetCinemaHallsOperation;
import sit.tu_varna.bg.api.operation.cinema.gethalls.GetCinemaHallsRequest;
import sit.tu_varna.bg.api.operation.showtime.getcinemabydate.GetCinemaShowtimesByDateOperation;
import sit.tu_varna.bg.api.operation.showtime.getcinemabydate.GetCinemaShowtimesByDateRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.time.LocalDate;
import java.util.UUID;

import static sit.tu_varna.bg.core.constants.BusinessConstants.ADMIN_ROLE;
import static sit.tu_varna.bg.core.constants.BusinessConstants.PROJECTOR_ROLE;

@Path("/api/cinemas")
@Authenticated
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
    @Inject
    GetCinemaHallsOperation getCinemaHallsOperation;
    @Inject
    GetCinemaShowtimesByDateOperation getCinemaShowtimesByDateOperation;

    @GET
    @PermitAll
    public Response getAllCinemas() {
        return Response.ok(getAllCinemasOperation.process(new GetAllCinemasRequest())).build();
    }

    @GET
    @PermitAll
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
    @RolesAllowed(ADMIN_ROLE)
    public Response addCinema(@Valid AddCinemaRequest addCinemaRequest) {
        return Response.ok(addCinemaOperation.process(addCinemaRequest)).build();
    }

    @PUT
    @RolesAllowed(ADMIN_ROLE)
    @Path("/{cinemaId}")
    public Response editCinema(@PathParam("cinemaId")
                               @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                       message = "Invalid UUID format")
                                       String cinemaId, @Valid EditCinemaRequest editCinemaRequest) {
        editCinemaRequest.setCinemaId(UUID.fromString(cinemaId));
        return Response.ok(editCinemaOperation.process(editCinemaRequest)).build();
    }

    @DELETE
    @RolesAllowed(ADMIN_ROLE)
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

    @GET
    @RolesAllowed({ADMIN_ROLE, PROJECTOR_ROLE})
    @Path("/{cinemaId}/halls")
    public Response getCinemaHalls(@PathParam("cinemaId")
                                   @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                           message = "Invalid UUID format")
                                           String cinemaId) {
        GetCinemaHallsRequest getCinemaHallsRequest = GetCinemaHallsRequest
                .builder()
                .cinemaId(UUID.fromString(cinemaId))
                .build();
        return Response.ok(getCinemaHallsOperation.process(getCinemaHallsRequest)).build();
    }

    @GET
    @PermitAll
    @Path("/{cinemaId}/showtimes")
    public Response getCinemaShowtimesByDate(@PathParam("cinemaId")
                                            @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                                    message = "Invalid UUID format")
                                                    String cinemaId,
                                            @QueryParam("date")
                                            @Pattern(regexp = ValidationConstants.LOCAL_DATE_REGEX,
                                                    message = "Invalid Local Date format")
                                                    String dateStr) {
        GetCinemaShowtimesByDateRequest request = GetCinemaShowtimesByDateRequest
                .builder()
                .cinemaId(UUID.fromString(cinemaId))
                .showtimeDate(LocalDate.parse(dateStr))
                .build();
        return Response.ok(getCinemaShowtimesByDateOperation.process(request)).build();
    }

}
