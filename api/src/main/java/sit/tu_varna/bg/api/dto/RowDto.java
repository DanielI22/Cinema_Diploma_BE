package sit.tu_varna.bg.api.dto;

import lombok.*;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RowDto {
    private String id;
    private Integer rowNumber;
    private Collection<SeatDto> seats;
}
