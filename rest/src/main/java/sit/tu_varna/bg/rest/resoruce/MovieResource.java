package sit.tu_varna.bg.rest.resoruce;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import sit.tu_varna.bg.api.operation.movie.GetAllMoviesOperation;
import sit.tu_varna.bg.api.operation.movie.GetAllMoviesRequest;

@Path("/api/movies")
public class MovieResource {
    @Inject
    GetAllMoviesOperation getAllMoviesOperation;

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    public Response getAllMovies() {
        return Response.ok(getAllMoviesOperation.process(new GetAllMoviesRequest())).build();
    }


//    @GET
//    @Path("/user")
//    @RolesAllowed({"client", "admin"})
//    @Produces(MediaType.APPLICATION_JSON)
//    public String getUserName() {
//        Principal principal = securityIdentity.getPrincipal();
//        return principal.getName();
//    }
//
//    @GET
//    @Produces(MediaType.APPLICATION_JSON)
//    @PermitAll
//    public Response test() {
//        return Response.ok("test").build();
//    }
//
//    @GET
//    @Path("client")
//    @RolesAllowed({"client", "admin"})
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response testClient() {
//        return Response.ok("test").build();
//    }
//
//    @GET
//    @Path("operator")
//    @RolesAllowed("operator")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response testOperator() {
//        return Response.ok("test").build();
//    }
//
//    @GET
//    @Path("admin")
//    @RolesAllowed("admin")
//    @Produces(MediaType.APPLICATION_JSON)
//    public Response testAdmin() {
//        return Response.ok("test").build();
//    }
}
