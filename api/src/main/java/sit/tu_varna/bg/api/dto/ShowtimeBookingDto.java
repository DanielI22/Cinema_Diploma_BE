package sit.tu_varna.bg.api.dto;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeBookingDto {
    private String id;
    private String userMail;
    private String status;
    private Collection<TicketDto> tickets;
}
