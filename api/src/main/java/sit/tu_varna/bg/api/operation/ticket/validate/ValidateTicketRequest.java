package sit.tu_varna.bg.api.operation.ticket.validate;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateTicketRequest implements ServiceRequest {
    private String shortCode;
    private UUID cinemaId;
    private UUID showtimeId;
}
