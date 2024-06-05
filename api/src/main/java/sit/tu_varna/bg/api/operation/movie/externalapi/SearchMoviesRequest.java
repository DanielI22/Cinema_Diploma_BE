package sit.tu_varna.bg.api.operation.movie.externalapi;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchMoviesRequest implements ServiceRequest {
    @NotBlank()
    private String query;
}
