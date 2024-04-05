package sit.tu_varna.bg.api.operation.movie.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMovieRequest implements ServiceRequest {
    private UUID movieId;
}
