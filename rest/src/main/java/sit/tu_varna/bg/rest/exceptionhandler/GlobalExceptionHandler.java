package sit.tu_varna.bg.rest.exceptionhandler;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
         if (exception instanceof ResourceNotFoundException) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(ErrorMessage.builder().message(exception.getMessage()).build())
                    .build();
        } else if (exception instanceof ResourceAlreadyExistsException) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(ErrorMessage.builder().message(exception.getMessage()).build())
                    .build();
        } else {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(ErrorMessage.builder().message("Internal Server Error").build())
                    .build();
        }
    }
}
