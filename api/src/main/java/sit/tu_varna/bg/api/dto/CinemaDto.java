package sit.tu_varna.bg.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CinemaDto {
    private String id;
    private String name;
    private String imageUrl;
    private String location;
}
