package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import sit.tu_varna.bg.api.operation.showtime.add.AddShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.add.AddShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.delete.DeleteShowtimeOperation;
import sit.tu_varna.bg.api.operation.showtime.delete.DeleteShowtimeRequest;
import sit.tu_varna.bg.api.operation.showtime.getall.GetShowtimesByDateOperation;
import sit.tu_varna.bg.api.operation.showtime.getall.GetShowtimesByDateRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.time.LocalDate;
import java.util.UUID;

@Path("/api/showtimes")
public class ShowtimeResource {
    @Inject
    GetShowtimesByDateOperation getShowtimesByDateOperation;
    @Inject
    AddShowtimeOperation addShowtimeOperation;
    @Inject
    DeleteShowtimeOperation deleteShowtimeOperation;

    @GET
    public Response getShowtimesByDate(@QueryParam("date")
                                       @Pattern(regexp = ValidationConstants.LOCAL_DATE_REGEX,
                                               message = "Invalid Local Date format")
                                               String dateStr) {
        GetShowtimesByDateRequest request = GetShowtimesByDateRequest
                .builder()
                .showtimeDate(LocalDate.parse(dateStr))
                .build();
        return Response.ok(getShowtimesByDateOperation.process(request)).build();
    }

    @POST
    public Response addShowtime(@Valid AddShowtimeRequest addShowtimeRequest) {
        return Response.ok(addShowtimeOperation.process(addShowtimeRequest)).build();
    }

    @DELETE
    @Path("/{showtimeId}")
    public Response deleteShowtime(@PathParam("showtimeId")
                                   @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                           message = "Invalid UUID format")
                                           String showtimeId) {
        DeleteShowtimeRequest deleteShowtimeRequest = DeleteShowtimeRequest
                .builder()
                .showtimeId(UUID.fromString(showtimeId))
                .build();
        return Response.ok(deleteShowtimeOperation.process(deleteShowtimeRequest)).build();
    }
}
