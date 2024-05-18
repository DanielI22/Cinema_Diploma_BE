package sit.tu_varna.bg.api.operation.booking.book;

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
public class BookingRequest implements ServiceRequest {
    @NotNull
    private OrderInfoDto orderInfo;
    private UUID userId;
}
