package sit.tu_varna.bg.api.dto;

import lombok.*;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoDto {
    private UUID showtimeId;
    private Collection<PurchaseSeatDto> seats;
    private UUID bookingId;
    private String language;
}
