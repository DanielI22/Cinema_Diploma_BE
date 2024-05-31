package sit.tu_varna.bg.api.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingDto {
    private String id;
    private String movieTitle;
    private String moviePoster;
    private String cinemaName;
    private String hallName;
    private String showtimeId;
    private LocalDateTime showtimeStartTime;
    private String status;
    private String shortcode;
    private Collection<ShowtimeTicketDto> tickets;
    private Double totalPrice;
}
