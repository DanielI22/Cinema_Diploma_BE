package sit.tu_varna.bg.api.operation.genre.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteGenreResponse implements ServiceResponse {
    private Boolean deleted;
}
