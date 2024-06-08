package sit.tu_varna.bg.core.operationservice.review;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.review.delete.DeleteReviewRequest;
import sit.tu_varna.bg.api.operation.review.delete.DeleteReviewResponse;
import sit.tu_varna.bg.entity.Review;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@QuarkusTest
class DeleteReviewServiceTest {

    DeleteReviewService deleteReviewService;

    UUID reviewId;
    Review mockReview;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(Review.class);
        deleteReviewService = new DeleteReviewService();

        reviewId = UUID.randomUUID();
        mockReview = mock(Review.class);
        when(mockReview.getId()).thenReturn(reviewId);
    }

    @Test
    void process_SuccessfulDeletion() {
        // Arrange
        when(Review.findByIdOptional(reviewId)).thenReturn(Optional.of(mockReview));
        when(mockReview.isPersistent()).thenReturn(true);

        DeleteReviewRequest request = new DeleteReviewRequest();
        request.setReviewId(reviewId);

        // Act
        DeleteReviewResponse response = deleteReviewService.process(request);

        // Assert
        assertNotNull(response);
        assertTrue(response.getDeleted());
        verify(mockReview, times(1)).delete();
    }

    @Test
    void process_ReviewNotFound() {
        // Arrange
        UUID reviewId = UUID.randomUUID();
        when(Review.findByIdOptional(reviewId)).thenReturn(Optional.empty());

        DeleteReviewRequest request = new DeleteReviewRequest();
        request.setReviewId(reviewId);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> deleteReviewService.process(request));
    }

    @Test
    void process_ReviewNotPersistent() {
        // Arrange
        when(Review.findByIdOptional(reviewId)).thenReturn(Optional.of(mockReview));
        when(mockReview.isPersistent()).thenReturn(false);

        DeleteReviewRequest request = new DeleteReviewRequest();
        request.setReviewId(reviewId);

        // Act
        DeleteReviewResponse response = deleteReviewService.process(request);

        // Assert
        assertTrue(response.getDeleted());
        verify(mockReview, never()).delete();
    }
}
