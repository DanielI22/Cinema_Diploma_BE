package sit.tu_varna.bg.api.dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseSeatDto {
    private UUID seatId;
    private TicketType ticketType;

    public enum TicketType {
        normal, reduced
    }
}
