package sit.tu_varna.bg.api.operation.genre.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteGenreRequest implements ServiceRequest {
    private UUID genreId;
}
