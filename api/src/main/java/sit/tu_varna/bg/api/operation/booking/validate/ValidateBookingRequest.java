package sit.tu_varna.bg.api.operation.booking.validate;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateBookingRequest implements ServiceRequest {
    private String shortCode;
    private UUID cinemaId;
}
