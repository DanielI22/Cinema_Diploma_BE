package sit.tu_varna.bg.rest.resource;

import io.quarkus.hibernate.validator.runtime.interceptor.MethodValidated;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.booking.book.BookingOperation;
import sit.tu_varna.bg.api.operation.booking.book.BookingRequest;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingOperation;
import sit.tu_varna.bg.api.operation.booking.cancel.CancelBookingRequest;
import sit.tu_varna.bg.api.operation.booking.cancelmy.CancelMyBookingOperation;
import sit.tu_varna.bg.api.operation.booking.cancelmy.CancelMyBookingRequest;
import sit.tu_varna.bg.api.operation.booking.getmybookings.GetMyBookingsOperation;
import sit.tu_varna.bg.api.operation.booking.getmybookings.GetMyBookingsRequest;
import sit.tu_varna.bg.api.operation.booking.getshowtime.GetShowtimeBookingsOperation;
import sit.tu_varna.bg.api.operation.booking.getshowtime.GetShowtimeBookingsRequest;
import sit.tu_varna.bg.api.operation.booking.take.TakeBookingOperation;
import sit.tu_varna.bg.api.operation.booking.take.TakeBookingRequest;
import sit.tu_varna.bg.api.operation.booking.validate.ValidateBookingOperation;
import sit.tu_varna.bg.api.operation.booking.validate.ValidateBookingRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

import static sit.tu_varna.bg.core.constants.BusinessConstants.*;

@Path("/api/bookings")
@Authenticated
public class BookingResource {
    @Inject
    GetMyBookingsOperation getMyBookingsOperation;
    @Inject
    GetShowtimeBookingsOperation getShowtimeBookingsOperation;
    @Inject
    BookingOperation bookingOperation;
    @Inject
    CancelBookingOperation cancelBookingOperation;
    @Inject
    CancelMyBookingOperation cancelMyBookingOperation;
    @Inject
    ValidateBookingOperation validateBookingOperation;
    @Inject
    TakeBookingOperation takeBookingOperation;
    @Inject
    @SuppressWarnings("all")
    JsonWebToken jwt;

    @GET
    @Path("my-bookings")
    public Response getMyBookings() {
        String userId = jwt.getClaim(USER_ID_CLAIM).toString();
        GetMyBookingsRequest request = GetMyBookingsRequest
                .builder()
                .userId(UUID.fromString(userId))
                .build();
        return Response.ok(getMyBookingsOperation.process(request)).build();
    }

    @GET
    @RolesAllowed(ADMIN_ROLE)
    @Path("showtimes/{showtimeId}")
    public Response getShowtimeBookings(@PathParam("showtimeId")
                                        @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                                message = "Invalid UUID format")
                                                String showtimeId) {
        GetShowtimeBookingsRequest request = GetShowtimeBookingsRequest
                .builder()
                .showtimeId(UUID.fromString(showtimeId))
                .build();
        return Response.ok(getShowtimeBookingsOperation.process(request)).build();
    }

    @POST
    public Response book(@Valid BookingRequest bookingRequest) {
        String userId = jwt.getClaim(USER_ID_CLAIM).toString();
        bookingRequest.setUserId(UUID.fromString(userId));
        return Response.ok(bookingOperation.process(bookingRequest)).build();
    }

    @PUT
    @RolesAllowed(ADMIN_ROLE)
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

    @PUT
    @Path("/{bookingId}/my")
    public Response cancelMyBooking(@PathParam("bookingId")
                                    @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                            message = "Invalid UUID format")
                                            String bookingId) {
        String userId = jwt.getClaim(USER_ID_CLAIM).toString();
        CancelMyBookingRequest cancelMyBookingRequest = CancelMyBookingRequest
                .builder()
                .bookingId(UUID.fromString(bookingId))
                .userId(UUID.fromString(userId))
                .build();
        return Response.ok(cancelMyBookingOperation.process(cancelMyBookingRequest)).build();
    }

    @GET
    @MethodValidated
    @RolesAllowed(OPERATOR_ROLE)
    @Path("validate/{bookingShortCode}")
    public Response validateBooking(@PathParam("bookingShortCode") @Size(min = 5, max = 5) String bookingShortCode,
                                    @QueryParam("cinema")
                                    @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                            message = "Invalid UUID format")
                                            String cinema) {
        ValidateBookingRequest request = ValidateBookingRequest
                .builder()
                .shortCode(bookingShortCode)
                .cinemaId(UUID.fromString(cinema))
                .build();
        return Response.ok(validateBookingOperation.process(request)).build();
    }

    @PUT
    @RolesAllowed(OPERATOR_ROLE)
    @Path("take/{bookingId}")
    public Response takeBooking(@PathParam("bookingId")
                                @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                        message = "Invalid UUID format")
                                        String bookingId) {
        String userId = jwt.getClaim(USER_ID_CLAIM).toString();
        TakeBookingRequest request = TakeBookingRequest
                .builder()
                .bookingId(UUID.fromString(bookingId))
                .userId(UUID.fromString(userId))
                .build();
        return Response.ok(takeBookingOperation.process(request)).build();
    }
}
