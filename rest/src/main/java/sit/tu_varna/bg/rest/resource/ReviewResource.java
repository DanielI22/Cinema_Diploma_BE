package sit.tu_varna.bg.rest.resource;

import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import sit.tu_varna.bg.api.operation.review.add.AddReviewOperation;
import sit.tu_varna.bg.api.operation.review.add.AddReviewRequest;
import sit.tu_varna.bg.api.operation.review.delete.DeleteReviewOperation;
import sit.tu_varna.bg.api.operation.review.delete.DeleteReviewRequest;
import sit.tu_varna.bg.api.operation.review.deletemy.DeleteMyReviewOperation;
import sit.tu_varna.bg.api.operation.review.deletemy.DeleteMyReviewRequest;
import sit.tu_varna.bg.api.operation.review.getbymovie.GetReviewsByMovieOperation;
import sit.tu_varna.bg.api.operation.review.getbymovie.GetReviewsByMovieRequest;
import sit.tu_varna.bg.core.constants.ValidationConstants;

import java.util.UUID;

import static sit.tu_varna.bg.core.constants.BusinessConstants.ADMIN_ROLE;

@Path("/api/reviews")
@Authenticated
public class ReviewResource {
    @Inject
    GetReviewsByMovieOperation getReviewsByMovieOperation;
    @Inject
    AddReviewOperation addReviewOperation;
    @Inject
    DeleteReviewOperation deleteReviewOperation;
    @Inject
    DeleteMyReviewOperation deleteMyReviewOperation;
    @Inject
    @SuppressWarnings("all")
    JsonWebToken jwt;

    @GET
    @PermitAll
    @Path("/{movieId}")
    public Response getAllMovieReviews(@PathParam("movieId")
                                       @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                               message = "Invalid UUID format")
                                               String movieId) {
        GetReviewsByMovieRequest request = GetReviewsByMovieRequest
                .builder()
                .movieId(UUID.fromString(movieId))
                .build();
        return Response.ok(getReviewsByMovieOperation.process(request)).build();
    }

    @POST
    @Path("/{movieId}")
    public Response addMovieReview(@PathParam("movieId")
                                   @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                           message = "Invalid UUID format")
                                           String movieId, @Valid AddReviewRequest addReviewRequest) {
        String userId = jwt.getClaim("sub").toString();
        addReviewRequest.setMovieId(UUID.fromString(movieId));
        addReviewRequest.setUserId(UUID.fromString(userId));
        return Response.ok(addReviewOperation.process(addReviewRequest)).build();
    }

    @DELETE
    @RolesAllowed(ADMIN_ROLE)
    @Path("/{reviewId}")
    public Response deleteReview(@PathParam("reviewId")
                                 @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                         message = "Invalid UUID format")
                                         String reviewId) {
        DeleteReviewRequest deleteReviewRequest = DeleteReviewRequest
                .builder()
                .reviewId(UUID.fromString(reviewId))
                .build();
        return Response.ok(deleteReviewOperation.process(deleteReviewRequest)).build();
    }

    @DELETE
    @Path("/{reviewId}")
    public Response deleteMyReview(@PathParam("reviewId")
                                 @Pattern(regexp = ValidationConstants.UUID_REGEX,
                                         message = "Invalid UUID format")
                                         String reviewId) {
        String userId = jwt.getClaim("sub").toString();
        DeleteMyReviewRequest deleteReviewRequest = DeleteMyReviewRequest
                .builder()
                .reviewId(UUID.fromString(reviewId))
                .userId(UUID.fromString(userId))
                .build();
        return Response.ok(deleteMyReviewOperation.process(deleteReviewRequest)).build();
    }
}
