package sit.tu_varna.bg.api.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private String id;
    private String userId;
    private String username;
    private String reviewText;
}
