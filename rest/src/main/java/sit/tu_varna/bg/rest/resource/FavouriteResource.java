package sit.tu_varna.bg.rest.resource;

import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.favourite.add.AddFavouriteOperation;
import sit.tu_varna.bg.api.operation.favourite.add.AddFavouriteRequest;
import sit.tu_varna.bg.api.operation.favourite.delete.DeleteFavouriteOperation;
import sit.tu_varna.bg.api.operation.favourite.delete.DeleteFavouriteRequest;
import sit.tu_varna.bg.api.operation.favourite.verify.VerifyFavouriteOperation;
import sit.tu_varna.bg.api.operation.favourite.verify.VerifyFavouriteRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

@Path("/api/favourites")
public class FavouriteResource {
    @Inject
    VerifyFavouriteOperation verifyFavouriteOperation;
    @Inject
    AddFavouriteOperation addFavouriteOperation;
    @Inject
    DeleteFavouriteOperation deleteFavouriteOperation;
    @Inject
    @SuppressWarnings("all")
    JsonWebToken jwt;

    @GET
    @Path("/{movieId}")
    public Response verifyFavourite(@PathParam("movieId")
                                    @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                            message = "Invalid UUID format")
                                            String movieId) {
        VerifyFavouriteRequest verifyFavouriteRequest = VerifyFavouriteRequest.builder().movieId(UUID.fromString(movieId)).build();
        String userId = jwt.getClaim("sub").toString();
        verifyFavouriteRequest.setUserId(UUID.fromString(userId));
        return Response.ok(verifyFavouriteOperation.process(verifyFavouriteRequest)).build();
    }

    @POST
    @Path("/{movieId}")
    public Response addFavourite(@PathParam("movieId")
                                 @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                         message = "Invalid UUID format")
                                         String movieId) {
        AddFavouriteRequest addFavouriteRequest = AddFavouriteRequest.builder().movieId(UUID.fromString(movieId)).build();
        String userId = jwt.getClaim("sub").toString();
        addFavouriteRequest.setUserId(UUID.fromString(userId));
        return Response.ok(addFavouriteOperation.process(addFavouriteRequest)).build();
    }

    @DELETE
    @Path("/{movieId}")
    public Response deleteFavourite(@PathParam("movieId")
                                    @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                            message = "Invalid UUID format")
                                            String movieId) {
        DeleteFavouriteRequest deleteFavouriteRequest = DeleteFavouriteRequest.builder().movieId(UUID.fromString(movieId)).build();
        String userId = jwt.getClaim("sub").toString();
        deleteFavouriteRequest.setUserId(UUID.fromString(userId));
        return Response.ok(deleteFavouriteOperation.process(deleteFavouriteRequest)).build();
    }
}
