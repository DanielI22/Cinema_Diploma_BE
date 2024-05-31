package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.ticket.get.GetTicketOperation;
import sit.tu_varna.bg.api.operation.ticket.get.GetTicketRequest;
import sit.tu_varna.bg.api.operation.ticket.getmy.GetMyTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.getmy.GetMyTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.getshowtime.GetShowtimePurchasedTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.getshowtime.GetShowtimePurchasedTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.history.HistoryTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.history.HistoryTicketsRequest;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsOperation;
import sit.tu_varna.bg.api.operation.ticket.purchase.AddTicketsRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

@Path("/api/tickets")
public class TicketResource {
    @Inject
    GetTicketOperation getTicketOperation;
    @Inject
    GetMyTicketsOperation getMyTicketsOperation;
    @Inject
    AddTicketsOperation addTicketsOperation;
    @Inject
    GetShowtimePurchasedTicketsOperation getShowtimePurchasedTicketsOperation;
    @Inject
    HistoryTicketsOperation historyTicketsOperation;
    @Inject
    @SuppressWarnings("all")
    JsonWebToken jwt;

    @GET
    @Path("/{ticketId}")
    public Response getTicket(@PathParam("ticketId")
                              @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                      message = "Invalid UUID format")
                                      String ticketId) {
        GetTicketRequest request = GetTicketRequest
                .builder()
                .ticketId(UUID.fromString(ticketId))
                .build();
        return Response.ok(getTicketOperation.process(request)).build();
    }

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

    @GET
    @Path("/history")
    public Response getOperatorHistory(@QueryParam("cinemaId")
                                       @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                               message = "Invalid UUID format")
                                               String cinemaId,
                                       @QueryParam("pageNumber") int pageNumber,
                                       @QueryParam("pageSize") int pageSize) {
        String userId = jwt.getClaim("sub").toString();
        HistoryTicketsRequest historyTicketsRequest = HistoryTicketsRequest.builder()
                .cinemaId(UUID.fromString(cinemaId))
                .userId(UUID.fromString(userId))
                .pageSize(pageSize)
                .pageNumber(pageNumber)
                .build();
        historyTicketsRequest.setUserId(UUID.fromString(userId));
        return Response.ok(historyTicketsOperation.process(historyTicketsRequest)).build();
    }


    @POST
    public Response add(@Valid AddTicketsRequest addTicketsRequest) {
        String userId = jwt.getClaim("sub").toString();
        addTicketsRequest.setUserId(UUID.fromString(userId));
        return Response.ok(addTicketsOperation.process(addTicketsRequest)).build();
    }
}
