package sit.tu_varna.bg.api.operation.favourite.verify;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyFavouriteRequest implements ServiceRequest {
    private UUID movieId;
    private UUID userId;
}
