package sit.tu_varna.bg.api.operation.showtime.add;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddShowtimeRequest implements ServiceRequest {
    @NotNull
    private UUID cinemaId;
    @NotNull
    private UUID hallId;
    @NotNull
    private UUID movieId;
    @NotNull
    private LocalDateTime startingTime;
    @NotNull
    private BigDecimal ticketPrice;
}
