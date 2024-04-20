package sit.tu_varna.bg.api.operation.review.getbymovie;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.ReviewDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetReviewsByMovieResponse implements ServiceResponse {
    private Collection<ReviewDto> reviews;
}
