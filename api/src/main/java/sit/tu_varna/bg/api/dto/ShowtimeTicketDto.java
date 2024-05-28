package sit.tu_varna.bg.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeTicketDto {
    private String id;
    private String type;
    private Double price;
    private String userMail;
    private SeatDto seat;
}
