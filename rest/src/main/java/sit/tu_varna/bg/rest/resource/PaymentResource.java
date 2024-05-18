package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.payment.PaymentOperation;
import sit.tu_varna.bg.api.operation.payment.PaymentRequest;

import java.util.UUID;

@Path("/api/payments")
public class PaymentResource {
    @Inject
    PaymentOperation paymentOperation;
    @Inject
    @SuppressWarnings("all")
    JsonWebToken jwt;

    @POST
    @Path("/create-payment-intent")
    public Response createPaymentIntent(@Valid PaymentRequest paymentRequest) {
        String userId = jwt.getClaim("sub").toString();
        paymentRequest.setUserId(UUID.fromString(userId));
        return Response.ok(paymentOperation.process(paymentRequest)).build();
    }
}
