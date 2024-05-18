package sit.tu_varna.bg.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatDto {
    private String id;
    private Integer seatNumber;
    private Boolean isEmpty;
    private Boolean isBooked;
}
