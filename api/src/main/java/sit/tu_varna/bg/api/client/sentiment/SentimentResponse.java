package sit.tu_varna.bg.api.client.sentiment;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SentimentResponse {
    private String sentiment;
}
