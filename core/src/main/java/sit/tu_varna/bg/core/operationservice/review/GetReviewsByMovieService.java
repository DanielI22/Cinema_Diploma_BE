package sit.tu_varna.bg.core.operationservice.review;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.ReviewDto;
import sit.tu_varna.bg.api.operation.review.getbymovie.GetReviewsByMovieOperation;
import sit.tu_varna.bg.api.operation.review.getbymovie.GetReviewsByMovieRequest;
import sit.tu_varna.bg.api.operation.review.getbymovie.GetReviewsByMovieResponse;
import sit.tu_varna.bg.core.mapper.ReviewMapper;
import sit.tu_varna.bg.entity.Review;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetReviewsByMovieService implements GetReviewsByMovieOperation {
    @Inject
    ReviewMapper reviewMapper;

    @Override
    public GetReviewsByMovieResponse process(GetReviewsByMovieRequest request) {
        List<ReviewDto> reviews = Review.findByMovieId(request.getMovieId())
                .stream()
                .sorted(Comparator.comparing(Review::getCreatedOn).reversed())
                .map(r -> reviewMapper.reviewToReviewDto(r))
                .collect(Collectors.toList());
        return GetReviewsByMovieResponse.builder()
                .reviews(reviews)
                .build();
    }
}
