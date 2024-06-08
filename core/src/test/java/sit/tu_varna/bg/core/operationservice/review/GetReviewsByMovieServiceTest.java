package sit.tu_varna.bg.core.operationservice.review;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.ReviewDto;
import sit.tu_varna.bg.api.operation.review.getbymovie.GetReviewsByMovieRequest;
import sit.tu_varna.bg.api.operation.review.getbymovie.GetReviewsByMovieResponse;
import sit.tu_varna.bg.core.mapper.ReviewMapper;
import sit.tu_varna.bg.entity.Movie;
import sit.tu_varna.bg.entity.Review;
import sit.tu_varna.bg.entity.User;
import sit.tu_varna.bg.enums.Sentiment;

import java.time.Instant;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
class GetReviewsByMovieServiceTest {

    @InjectMocks
    GetReviewsByMovieService getReviewsByMovieService;

    @Mock
    ReviewMapper reviewMapper;

    UUID movieId;
    Movie movie;
    Review review1;
    Review review2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(Review.class);

        movieId = UUID.randomUUID();

        movie = new Movie();
        movie.setId(movieId);
        movie.setTitle("Test Movie");

        review1 = new Review();
        review1.setId(UUID.randomUUID());
        review1.setReviewText("Great movie!");
        review1.setMovie(movie);
        review1.setUser(User.builder().id(UUID.randomUUID()).build());
        review1.setSentiment(Sentiment.NEGATIVE);
        review1.setCreatedOn(Instant.now().minusSeconds(3600)); // 1 hour ago

        review2 = new Review();
        review2.setId(UUID.randomUUID());
        review2.setReviewText("Not bad.");
        review2.setMovie(movie);
        review2.setUser(User.builder().id(UUID.randomUUID()).build());
        review2.setSentiment(Sentiment.POSITIVE);
        review2.setCreatedOn(Instant.now().minusSeconds(1800)); // 30 minutes ago
    }

    @Test
    void process_ShouldReturnSortedReviews() {
        // Arrange
        when(Review.findByMovieId(movieId)).thenReturn(Arrays.asList(review1, review2));
        when(reviewMapper.reviewToReviewDto(review1)).thenReturn(new ReviewDto(review1.getId().toString(), review1.getUser().getId().toString(), review1.getUser().getEmail(), review1.getReviewText(), review1.getSentiment().toString().toLowerCase()));
        when(reviewMapper.reviewToReviewDto(review2)).thenReturn(new ReviewDto(review2.getId().toString(), review2.getUser().getId().toString(), review2.getUser().getEmail(), review2.getReviewText(), review2.getSentiment().toString().toLowerCase()));

        GetReviewsByMovieRequest request = new GetReviewsByMovieRequest();
        request.setMovieId(movieId);

        // Act
        GetReviewsByMovieResponse response = getReviewsByMovieService.process(request);

        // Assert
        Collection<ReviewDto> reviews = response.getReviews();
        assertEquals(2, reviews.size());
        assertEquals("Not bad.", Objects.requireNonNull(reviews.stream().findFirst().orElse(null)).getReviewText()); // Latest review first
        assertEquals("Great movie!", Objects.requireNonNull(reviews.stream().skip(1).findFirst().orElse(null)).getReviewText()); // Oldest review last

        verify(reviewMapper, times(1)).reviewToReviewDto(review1);
        verify(reviewMapper, times(1)).reviewToReviewDto(review2);
    }
}
