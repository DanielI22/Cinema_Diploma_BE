package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.ReviewDto;
import sit.tu_varna.bg.core.externalservice.KeycloakService;
import sit.tu_varna.bg.entity.Review;

@ApplicationScoped
public class ReviewMapper {
    @Inject
    KeycloakService keycloakService;

    public ReviewDto reviewToReviewDto(Review review) {
        return ReviewDto.builder()
                .id(review.getId().toString())
                .userId(review.getUser().getId().toString())
                .username(keycloakService.getUserRepresentation(review.getUser().getId().toString()).getUsername())
                .reviewText(review.getReviewText())
                .build();
    }
}
