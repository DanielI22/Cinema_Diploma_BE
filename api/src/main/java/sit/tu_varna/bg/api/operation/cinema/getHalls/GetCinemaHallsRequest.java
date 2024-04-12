package sit.tu_varna.bg.api.operation.cinema.getHalls;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCinemaHallsRequest implements ServiceRequest {
    private UUID cinemaId;
}
