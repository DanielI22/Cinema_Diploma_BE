package sit.tu_varna.bg.rest.resource;

import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.user.login.LoginOperation;
import sit.tu_varna.bg.api.operation.user.login.LoginRequest;
import sit.tu_varna.bg.api.operation.user.logout.LogoutOperation;
import sit.tu_varna.bg.api.operation.user.logout.LogoutRequest;
import sit.tu_varna.bg.api.operation.user.refresh.RefreshOperation;
import sit.tu_varna.bg.api.operation.user.refresh.RefreshRequest;
import sit.tu_varna.bg.api.operation.user.register.RegisterOperation;
import sit.tu_varna.bg.api.operation.user.register.RegisterRequest;

@Path("/api/users")
public class UserResource {
    @Inject
    RegisterOperation registerOperation;
    @Inject
    LoginOperation loginOperation;
    @Inject
    LogoutOperation logoutOperation;
    @Inject
    RefreshOperation refreshOperation;
    @Inject
    @SuppressWarnings("all")
    JsonWebToken jwt;

    @POST
    @Path("/register")
    public Response register(@Valid RegisterRequest request) {
        return Response.ok(registerOperation.process(request)).build();
    }

    @POST
    @Path("/login")
    public Response login(@Valid LoginRequest request) {
        return Response.ok(loginOperation.process(request)).build();
    }

    @POST
    @Path("/logout")
    @Authenticated
    public Response logout() {
        String userId = jwt.getClaim("sub").toString();
        LogoutRequest request = LogoutRequest.builder().userId(userId).build();
        return Response.ok(logoutOperation.process(request)).build();
    }

    @POST
    @Path("/refresh")
    @Authenticated
    public Response refresh(@Valid RefreshRequest request) {
        return Response.ok(refreshOperation.process(request)).build();
    }
}
