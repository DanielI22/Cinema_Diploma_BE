package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.ticket.getmytickets.GetMyTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.getmytickets.GetMyTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.getshowtime.GetShowtimePurchasedTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.getshowtime.GetShowtimePurchasedTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

@Path("/api/tickets")
public class TicketResource {
    @Inject
    GetMyTicketsOperation getMyTicketsOperation;
    @Inject
    AddTicketsOperation addTicketsOperation;
    @Inject
    GetShowtimePurchasedTicketsOperation getShowtimePurchasedTicketsOperation;
    @Inject
    @SuppressWarnings("all")
    JsonWebToken jwt;

    @GET
    @Path("my-tickets")
    public Response getMyTickets() {
        String userId = jwt.getClaim("sub").toString();
        GetMyTicketsRequest request = GetMyTicketsRequest
                .builder()
                .userId(UUID.fromString(userId))
                .build();
        return Response.ok(getMyTicketsOperation.process(request)).build();
    }

    @GET
    @Path("showtimes/{showtimeId}")
    public Response getShowtimeTickets(@PathParam("showtimeId")
                                       @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                               message = "Invalid UUID format")
                                               String showtimeId) {
        GetShowtimePurchasedTicketsRequest request = GetShowtimePurchasedTicketsRequest
                .builder()
                .showtimeId(UUID.fromString(showtimeId))
                .build();
        return Response.ok(getShowtimePurchasedTicketsOperation.process(request)).build();
    }


    @POST
    public Response add(@Valid AddTicketsRequest addTicketsRequest) {
        String userId = jwt.getClaim("sub").toString();
        addTicketsRequest.setUserId(UUID.fromString(userId));
        return Response.ok(addTicketsOperation.process(addTicketsRequest)).build();
    }
}
