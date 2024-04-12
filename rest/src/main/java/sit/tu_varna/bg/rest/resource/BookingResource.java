package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingOperation;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingRequest;
import sit.tu_varna.bg.api.operation.booking.getall.GetAllBookingsOperation;
import sit.tu_varna.bg.api.operation.booking.getall.GetAllBookingsRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

@Path("/api/bookings")
public class BookingResource {
    @Inject
    GetAllBookingsOperation getAllBookingsOperation;
    @Inject
    CancelBookingOperation cancelBookingOperation;

    @GET
    public Response getAllBookings() {
        return Response.ok(getAllBookingsOperation.process(new GetAllBookingsRequest())).build();
    }

    @DELETE
    @Path("/{bookingId}")
    public Response cancelBooking(@PathParam("bookingId")
                                @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                        message = "Invalid UUID format")
                                        String bookingId) {
        CancelBookingRequest cancelBookingRequest = CancelBookingRequest
                .builder()
                .bookingId(UUID.fromString(bookingId))
                .build();
        return Response.ok(cancelBookingOperation.process(cancelBookingRequest)).build();
    }
}
