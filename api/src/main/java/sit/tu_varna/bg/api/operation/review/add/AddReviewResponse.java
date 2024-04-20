package sit.tu_varna.bg.api.operation.review.add;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.ReviewDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewResponse implements ServiceResponse {
    private ReviewDto review;
}
