package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsRequest;

import java.util.UUID;

@Path("/api/tickets")
public class TicketResource {
    @Inject
    AddTicketsOperation addTicketsOperation;
    @Inject
    @SuppressWarnings("all")
    JsonWebToken jwt;

    @POST
    public Response add(@Valid AddTicketsRequest addTicketsRequest) {
        String userId = jwt.getClaim("sub").toString();
        addTicketsRequest.setUserId(UUID.fromString(userId));
        return Response.ok(addTicketsOperation.process(addTicketsRequest)).build();
    }
}
