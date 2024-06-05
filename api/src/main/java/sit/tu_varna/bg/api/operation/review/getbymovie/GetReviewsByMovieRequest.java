package sit.tu_varna.bg.api.operation.review.getbymovie;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetReviewsByMovieRequest implements ServiceRequest {
    private UUID movieId;
}
