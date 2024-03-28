package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import sit.tu_varna.bg.api.operation.hall.getall.GetAllHallsOperation;
import sit.tu_varna.bg.api.operation.hall.getall.GetAllHallsRequest;
import sit.tu_varna.bg.api.operation.hall.getavailable.GetAvailableHallsOperation;
import sit.tu_varna.bg.api.operation.hall.getavailable.GetAvailableHallsRequest;

@Path("/api/halls")
public class HallResource {
    @Inject
    GetAvailableHallsOperation getAvailableHallsOperation;
    @Inject
    GetAllHallsOperation getAllHallsOperation;

    @GET
    public Response getAllHalls() {
        return Response.ok(getAllHallsOperation.process(new GetAllHallsRequest())).build();
    }
    @GET
    @Path("/available")
    public Response getAvailableHalls() {
        return Response.ok(getAvailableHallsOperation.process(new GetAvailableHallsRequest())).build();
    }
}
