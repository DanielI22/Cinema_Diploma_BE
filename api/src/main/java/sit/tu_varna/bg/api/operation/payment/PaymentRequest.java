package sit.tu_varna.bg.api.operation.payment;

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
public class PaymentRequest implements ServiceRequest {
    @NotNull
    private String paymentMethodId;
    @NotNull
    private OrderInfoDto orderInfo;
    private UUID userId;
}
