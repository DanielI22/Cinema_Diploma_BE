package sit.tu_varna.bg.api.dto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OperatorTicketDto {
    private String id;
    private String shortcode;
    private String movieTitle;
    private LocalDateTime showtimeStartTime;
    private String type;
    private Double price;
    private LocalDateTime soldTime;
}
