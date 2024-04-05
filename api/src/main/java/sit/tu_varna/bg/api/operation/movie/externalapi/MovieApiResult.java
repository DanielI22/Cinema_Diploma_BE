package sit.tu_varna.bg.api.operation.movie.externalapi;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MovieApiResult {
    private Integer id;
    private String title;
    private String overview;
    public int runtime;
    private LocalDate release_date;
    private String poster_path;
    private VideoApiResult videos;
    private List<GenreApiResult> genres;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class GenreApiResult {
        public String name;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VideoApiResult {
        public List<VideoSingleApiResult> results;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VideoSingleApiResult {
        public String key;
    }
}
