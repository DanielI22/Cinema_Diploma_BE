package sit.tu_varna.bg.api.operation.movie.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMovieResponse implements ServiceResponse {
    private Boolean deleted;
}
