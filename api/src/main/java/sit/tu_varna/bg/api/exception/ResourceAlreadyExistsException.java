package sit.tu_varna.bg.api.exception;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

public class ResourceAlreadyExistsException extends WebApplicationException {
    public ResourceAlreadyExistsException(String message) {
        super(Response.status(Response.Status.BAD_REQUEST).entity(message).type(MediaType.TEXT_PLAIN).build());
    }
}
