package sit.tu_varna.bg.api.operation.favourite.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFavouriteRequest implements ServiceRequest {
    private UUID movieId;
    private UUID userId;
}
