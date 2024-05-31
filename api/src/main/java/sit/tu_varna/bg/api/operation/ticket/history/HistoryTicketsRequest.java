package sit.tu_varna.bg.api.operation.ticket.history;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryTicketsRequest implements ServiceRequest {
    private UUID userId;
    @NotNull
    private UUID cinemaId;
    @NotNull
    private int pageNumber;
    @NotNull
    private int pageSize;
}
