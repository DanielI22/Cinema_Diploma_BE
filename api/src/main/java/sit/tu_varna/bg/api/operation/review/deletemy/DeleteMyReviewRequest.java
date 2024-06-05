package sit.tu_varna.bg.api.operation.review.deletemy;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMyReviewRequest implements ServiceRequest {
    @NonNull
    private UUID reviewId;
    private UUID userId;
}
