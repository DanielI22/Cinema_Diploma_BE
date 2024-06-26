package sit.tu_varna.bg.rest.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.payment.PaymentOperation;
import sit.tu_varna.bg.api.operation.payment.PaymentRequest;

import java.util.UUID;

import static sit.tu_varna.bg.core.constants.BusinessConstants.USER_ID_CLAIM;

@Path("/api/payments")
@Authenticated
public class PaymentResource {
    @Inject
    PaymentOperation paymentOperation;
    @Inject
    @SuppressWarnings("all")
    JsonWebToken jwt;

    @POST
    @Path("/create-payment-intent")
    public Response createPaymentIntent(@Valid PaymentRequest paymentRequest) {
        String userId = jwt.getClaim(USER_ID_CLAIM).toString();
        paymentRequest.setUserId(UUID.fromString(userId));
        return Response.ok(paymentOperation.process(paymentRequest)).build();
    }
}
