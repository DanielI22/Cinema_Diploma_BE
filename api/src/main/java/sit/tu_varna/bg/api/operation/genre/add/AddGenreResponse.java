package sit.tu_varna.bg.api.operation.genre.add;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddGenreResponse implements ServiceResponse {
    private String genreId;
}
