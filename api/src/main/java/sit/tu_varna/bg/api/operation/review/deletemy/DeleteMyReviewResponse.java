package sit.tu_varna.bg.api.operation.review.deletemy;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMyReviewResponse implements ServiceResponse {
    private Boolean deleted;
}
