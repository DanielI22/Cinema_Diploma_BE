package sit.tu_varna.bg.api.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowtimeDto {
    private String id;
    private String cinemaName;
    private String movieName;
    private Integer movieDuration;
    private String movieId;
    private String hallName;
    private LocalDateTime startTime;
    private String type;
    private Double ticketPrice;
    private Boolean isCurrent;
    private Boolean isEnded;
}
