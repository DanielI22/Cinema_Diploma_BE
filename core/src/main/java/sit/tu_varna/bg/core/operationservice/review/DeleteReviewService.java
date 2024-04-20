package sit.tu_varna.bg.core.operationservice.review;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.review.delete.DeleteReviewOperation;
import sit.tu_varna.bg.api.operation.review.delete.DeleteReviewRequest;
import sit.tu_varna.bg.api.operation.review.delete.DeleteReviewResponse;
import sit.tu_varna.bg.entity.Review;

@ApplicationScoped
public class DeleteReviewService implements DeleteReviewOperation {

    @Transactional
    @Override
    public DeleteReviewResponse process(DeleteReviewRequest request) {
        Review review = (Review) Review.findByIdOptional(request.getReviewId())
                .orElseThrow(() -> new ResourceNotFoundException("Review does not exist"));
        if (review.isPersistent()) {
            review.delete();
        }
        return DeleteReviewResponse.builder().deleted(true).build();
    }
}
