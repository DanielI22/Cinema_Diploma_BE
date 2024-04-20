package sit.tu_varna.bg.api.operation.favourite.delete;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteFavouriteResponse implements ServiceResponse {
    private Boolean deleted;
}
