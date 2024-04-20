package sit.tu_varna.bg.api.operation.favourite.add;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddFavouriteResponse implements ServiceResponse {
    private Boolean added;
}
