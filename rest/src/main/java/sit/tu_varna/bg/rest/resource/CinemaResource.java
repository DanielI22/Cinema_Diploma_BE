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
import sit.tu_varna.bg.api.operation.cinema.get.GetCinemaOperation;
import sit.tu_varna.bg.api.operation.cinema.get.GetCinemaRequest;
import sit.tu_varna.bg.api.operation.cinema.getHalls.GetCinemaHallsOperation;
import sit.tu_varna.bg.api.operation.cinema.getHalls.GetCinemaHallsRequest;
import sit.tu_varna.bg.api.operation.cinema.getall.GetAllCinemasOperation;
import sit.tu_varna.bg.api.operation.cinema.getall.GetAllCinemasRequest;
import sit.tu_varna.bg.api.operation.showtime.getcinemaall.GetCinemaShowtimesByDateOperation;
import sit.tu_varna.bg.api.operation.showtime.getcinemaall.GetCinemaShowtimesByDateRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.time.LocalDate;
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
    @Inject
    GetCinemaHallsOperation getCinemaHallsOperation;
    @Inject
    GetCinemaShowtimesByDateOperation getCinemaShowtimesByDateOperation;

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

    @GET
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
                .cinemaId(cinemaId)
                .showtimeDate(LocalDate.parse(dateStr))
                .build();
        return Response.ok(getCinemaShowtimesByDateOperation.process(request)).build();
    }

}
