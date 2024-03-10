package sit.tu_varna.bg.rest;

import io.quarkus.security.Authenticated;
import io.quarkus.security.identity.SecurityIdentity;
import sit.tu_varna.bg.core.TestService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.security.Principal;

@Path("/test")
@Authenticated
public class ResourceTest {
    @Inject
    TestService testService;

    @Inject
    SecurityIdentity securityIdentity;

    @GET
    @Path("/user")
    @RolesAllowed({"client", "admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public String getUserName() {
        Principal principal = securityIdentity.getPrincipal();
        return principal.getName();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PermitAll
    public Response test() {
        return Response.ok("test").build();
    }

    @GET
    @Path("client")
    @RolesAllowed({"client", "admin"})
    @Produces(MediaType.APPLICATION_JSON)
    public Response testClient() {
        return Response.ok("test").build();
    }

    @GET
    @Path("operator")
    @RolesAllowed("operator")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testOperator() {
        return Response.ok("test").build();
    }

    @GET
    @Path("admin")
    @RolesAllowed("admin")
    @Produces(MediaType.APPLICATION_JSON)
    public Response testAdmin() {
        return Response.ok("test").build();
    }
}
