package sit.tu_varna.bg.api.operation.review.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteReviewRequest implements ServiceRequest {
    private UUID reviewId;
}
