package sit.tu_varna.bg.api.operation.cinema.edit;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditCinemaResponse implements ServiceResponse {
    private String cinemaId;
}
