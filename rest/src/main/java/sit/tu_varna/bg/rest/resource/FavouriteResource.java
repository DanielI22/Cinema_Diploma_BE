package sit.tu_varna.bg.rest.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.favourite.add.AddFavouriteOperation;
import sit.tu_varna.bg.api.operation.favourite.add.AddFavouriteRequest;
import sit.tu_varna.bg.api.operation.favourite.delete.DeleteFavouriteOperation;
import sit.tu_varna.bg.api.operation.favourite.delete.DeleteFavouriteRequest;
import sit.tu_varna.bg.api.operation.favourite.getall.GetFavouritesOperation;
import sit.tu_varna.bg.api.operation.favourite.getall.GetFavouritesRequest;
import sit.tu_varna.bg.api.operation.favourite.verify.VerifyFavouriteOperation;
import sit.tu_varna.bg.api.operation.favourite.verify.VerifyFavouriteRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

import static sit.tu_varna.bg.core.constants.BusinessConstants.USER_ROLE;

@Path("/api/favourites")
@Authenticated
public class FavouriteResource {
    @Inject
    GetFavouritesOperation getFavouritesOperation;
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
    public Response getFavourites() {
        String userId = jwt.getClaim("sub").toString();
        GetFavouritesRequest getFavouritesRequest = GetFavouritesRequest.builder().userId(UUID.fromString(userId)).build();
        return Response.ok(getFavouritesOperation.process(getFavouritesRequest)).build();
    }

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
