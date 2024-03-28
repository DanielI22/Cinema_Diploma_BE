package sit.tu_varna.bg.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HallCinemaDto {
    private String id;
    private String name;
    private String cinemaName;
}
