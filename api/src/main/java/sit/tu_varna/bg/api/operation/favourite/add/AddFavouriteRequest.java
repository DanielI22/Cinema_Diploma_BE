package sit.tu_varna.bg.api.operation.favourite.add;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddFavouriteRequest implements ServiceRequest {
    private UUID movieId;
    private UUID userId;
}
