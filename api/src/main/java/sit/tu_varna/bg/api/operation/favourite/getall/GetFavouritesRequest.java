package sit.tu_varna.bg.api.operation.favourite.getall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFavouritesRequest implements ServiceRequest {
    private UUID userId;
}
