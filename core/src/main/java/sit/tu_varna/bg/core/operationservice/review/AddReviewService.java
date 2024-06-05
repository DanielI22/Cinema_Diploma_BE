package sit.tu_varna.bg.core.operationservice.review;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.review.add.AddReviewOperation;
import sit.tu_varna.bg.api.operation.review.add.AddReviewRequest;
import sit.tu_varna.bg.api.operation.review.add.AddReviewResponse;
import sit.tu_varna.bg.core.mapper.ReviewMapper;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.Review;
import sit.tu_varna.bg.entity.User;

import java.util.UUID;

@ApplicationScoped
public class AddReviewService implements AddReviewOperation {
    @Inject
    ReviewMapper reviewMapper;

    @Transactional
    @Override
    public AddReviewResponse process(AddReviewRequest request) {
        UUID userId = request.getUserId();
        UUID movieId = request.getMovieId();

        User user = (User) User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        Movie movie = (Movie) Movie.findByIdOptional(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id " + movieId + " not found"));

        Review review = Review.builder()
                .movie(movie)
                .user(user)
                .reviewText(request.getReviewText())
                .build();

        review.persist();

        return AddReviewResponse.builder()
                .review(reviewMapper.reviewToReviewDto(review))
                .build();
    }
}
