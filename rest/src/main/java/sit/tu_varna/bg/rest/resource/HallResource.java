package sit.tu_varna.bg.rest.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import sit.tu_varna.bg.api.operation.hall.add.AddHallOperation;
import sit.tu_varna.bg.api.operation.hall.add.AddHallRequest;
import sit.tu_varna.bg.api.operation.hall.delete.DeleteHallOperation;
import sit.tu_varna.bg.api.operation.hall.delete.DeleteHallRequest;
import sit.tu_varna.bg.api.operation.hall.edit.EditHallOperation;
import sit.tu_varna.bg.api.operation.hall.edit.EditHallRequest;
import sit.tu_varna.bg.api.operation.hall.get.GetHallOperation;
import sit.tu_varna.bg.api.operation.hall.get.GetHallRequest;
import sit.tu_varna.bg.api.operation.hall.getall.GetAllHallsOperation;
import sit.tu_varna.bg.api.operation.hall.getall.GetAllHallsRequest;
import sit.tu_varna.bg.api.operation.hall.getavailable.GetAvailableHallsOperation;
import sit.tu_varna.bg.api.operation.hall.getavailable.GetAvailableHallsRequest;
import sit.tu_varna.bg.api.operation.hall.getshowtimehall.GetShowtimeHallOperation;
import sit.tu_varna.bg.api.operation.hall.getshowtimehall.GetShowtimeHallRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

import static sit.tu_varna.bg.core.constants.BusinessConstants.ADMIN_ROLE;

@Path("/api/halls")
@Authenticated
public class HallResource {
    @Inject
    GetAvailableHallsOperation getAvailableHallsOperation;
    @Inject
    GetAllHallsOperation getAllHallsOperation;
    @Inject
    GetHallOperation getHallOperation;
    @Inject
    GetShowtimeHallOperation getShowtimeHallOperation;
    @Inject
    AddHallOperation addHallOperation;
    @Inject
    EditHallOperation editHallOperation;
    @Inject
    DeleteHallOperation deleteHallOperation;

    @GET
    @RolesAllowed(ADMIN_ROLE)
    public Response getAllHalls() {
        return Response.ok(getAllHallsOperation.process(new GetAllHallsRequest())).build();
    }

    @GET
    @RolesAllowed(ADMIN_ROLE)
    @Path("/available")
    public Response getAvailableHalls() {
        return Response.ok(getAvailableHallsOperation.process(new GetAvailableHallsRequest())).build();
    }

    @GET
    @RolesAllowed(ADMIN_ROLE)
    @Path("/{hallId}")
    public Response getHall(@PathParam("hallId")
                            @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                    message = "Invalid UUID format")
                                    String hallId) {
        GetHallRequest request = GetHallRequest
                .builder()
                .hallId(UUID.fromString(hallId))
                .build();
        return Response.ok(getHallOperation.process(request)).build();
    }

    @GET
    @Path("/showtimes/{showtimeId}")
    public Response getShowtimeHall(@PathParam("showtimeId")
                                    @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                            message = "Invalid UUID format")
                                            String showtimeId) {
        GetShowtimeHallRequest request = GetShowtimeHallRequest
                .builder()
                .showtimeId(UUID.fromString(showtimeId))
                .build();
        return Response.ok(getShowtimeHallOperation.process(request)).build();
    }

    @POST
    @RolesAllowed(ADMIN_ROLE)
    public Response addHall(@Valid AddHallRequest addHallRequest) {
        return Response.ok(addHallOperation.process(addHallRequest)).build();
    }

    @PUT
    @RolesAllowed(ADMIN_ROLE)
    @Path("/{hallId}")
    public Response editHall(@PathParam("hallId")
                               @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                       message = "Invalid UUID format")
                                       String hallId, @Valid EditHallRequest editHallRequest) {
        editHallRequest.setHallId(UUID.fromString(hallId));
        return Response.ok(editHallOperation.process(editHallRequest)).build();
    }

    @DELETE
    @RolesAllowed(ADMIN_ROLE)
    @Path("/{hallId}")
    public Response deleteHall(@PathParam("hallId")
                               @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                       message = "Invalid UUID format")
                                       String hallId) {
        DeleteHallRequest deleteHallRequest = DeleteHallRequest
                .builder()
                .hallId(UUID.fromString(hallId))
                .build();
        return Response.ok(deleteHallOperation.process(deleteHallRequest)).build();
    }
}
