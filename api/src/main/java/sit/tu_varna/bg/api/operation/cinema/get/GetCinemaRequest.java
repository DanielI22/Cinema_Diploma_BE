package sit.tu_varna.bg.api.operation.cinema.get;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCinemaRequest implements ServiceRequest {
    private UUID cinemaId;
}
