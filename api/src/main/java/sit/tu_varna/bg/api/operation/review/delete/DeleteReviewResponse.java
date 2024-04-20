package sit.tu_varna.bg.api.operation.review.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteReviewResponse implements ServiceResponse {
    private Boolean deleted;
}
