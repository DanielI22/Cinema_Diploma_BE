package sit.tu_varna.bg.core.operationservice.review;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.review.deletemy.DeleteMyReviewOperation;
import sit.tu_varna.bg.api.operation.review.deletemy.DeleteMyReviewRequest;
import sit.tu_varna.bg.api.operation.review.deletemy.DeleteMyReviewResponse;
import sit.tu_varna.bg.entity.Review;

import java.util.UUID;

@ApplicationScoped
public class DeleteMyReviewService implements DeleteMyReviewOperation {

    @Transactional
    @Override
    public DeleteMyReviewResponse process(DeleteMyReviewRequest request) {
        UUID reviewId = request.getReviewId();
        Review review = (Review) Review.findByIdOptional(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review with id " + reviewId + " not found"));
        if (review.isPersistent() && review.getUser().getId().equals(request.getUserId())) {
            review.delete();
        }
        return DeleteMyReviewResponse.builder().deleted(true).build();
    }
}
