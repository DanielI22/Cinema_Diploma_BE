package sit.tu_varna.bg.api.operation.movie.getbyid;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMovieRequest implements ServiceRequest {
    private UUID movieId;
}
