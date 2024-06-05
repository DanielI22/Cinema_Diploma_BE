package sit.tu_varna.bg.api.operation.review.add;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddReviewRequest implements ServiceRequest {
    private UUID movieId;
    private UUID userId;
    @NotBlank
    private String reviewText;
}
