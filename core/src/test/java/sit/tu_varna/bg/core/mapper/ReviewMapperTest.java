package sit.tu_varna.bg.core.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.ReviewDto;
import sit.tu_varna.bg.core.common.KeycloakService;
import sit.tu_varna.bg.entity.Review;
import sit.tu_varna.bg.entity.User;
import sit.tu_varna.bg.enums.Sentiment;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ReviewMapperTest {

    @InjectMocks
    ReviewMapper reviewMapper;

    @Mock
    KeycloakService keycloakService;

    Review review;
    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        UUID userId = UUID.randomUUID();
        UUID reviewId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .build();

        review = Review.builder()
                .id(reviewId)
                .user(user)
                .reviewText("This is a review.")
                .sentiment(Sentiment.POSITIVE)
                .build();

        UserRepresentation userRepresentation = new UserRepresentation();
        userRepresentation.setId(userId.toString());
        userRepresentation.setUsername("testuser");

        when(keycloakService.getUserRepresentation(userId.toString())).thenReturn(userRepresentation);
    }

    @Test
    void testReviewToReviewDto() {
        ReviewDto reviewDto = reviewMapper.reviewToReviewDto(review);

        assertEquals(review.getId().toString(), reviewDto.getId());
        assertEquals(review.getUser().getId().toString(), reviewDto.getUserId());
        assertEquals("testuser", reviewDto.getUsername());
        assertEquals(review.getReviewText(), reviewDto.getReviewText());
        assertEquals(review.getSentiment().toString().toLowerCase(), reviewDto.getSentiment());
    }
}
