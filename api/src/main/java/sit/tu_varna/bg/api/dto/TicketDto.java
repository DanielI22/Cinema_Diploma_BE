package sit.tu_varna.bg.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketDto {
    private String id;
    private String shortcode;
    private String movieTitle;
    private String moviePoster;
    private String cinemaName;
    private LocalDateTime showtimeStartTime;
    private String type;
    private Double price;
    private SeatDto seat;
}
