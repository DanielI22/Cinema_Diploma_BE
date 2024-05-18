package sit.tu_varna.bg.api.operation.payment;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponse implements ServiceResponse {
    private Map<String, String> paymentResponse;
}
