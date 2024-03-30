package sit.tu_varna.bg.api.operation.hall.get;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetHallRequest implements ServiceRequest {
    private UUID hallId;
}
