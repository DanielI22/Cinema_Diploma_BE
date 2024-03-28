package sit.tu_varna.bg.api.operation.cinema.add;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCinemaResponse implements ServiceResponse {
    private String cinemaId;
}
