package sit.tu_varna.bg.api.operation.favourite.verify;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerifyFavouriteResponse implements ServiceResponse {
    private Boolean isFavourite;
}
