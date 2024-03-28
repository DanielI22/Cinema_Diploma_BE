package sit.tu_varna.bg.api.operation.hall.getall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllHallsRequest implements ServiceRequest {
    private UUID cinemaId;
}
