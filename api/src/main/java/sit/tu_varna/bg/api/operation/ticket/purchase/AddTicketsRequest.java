package sit.tu_varna.bg.api.operation.ticket.purchase;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;
import sit.tu_varna.bg.api.dto.OrderInfoDto;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddTicketsRequest implements ServiceRequest {
    @NotNull
    private OrderInfoDto orderInfo;
    private UUID userId;
}
