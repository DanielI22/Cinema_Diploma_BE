package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import sit.tu_varna.bg.api.operation.user.register.RegisterOperation;
import sit.tu_varna.bg.api.operation.user.register.RegisterRequest;

@Path("/api/users")
public class UserResource {
    @Inject
    RegisterOperation registerOperation;

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        return Response.ok(registerOperation.process(request)).build();
    }
}
