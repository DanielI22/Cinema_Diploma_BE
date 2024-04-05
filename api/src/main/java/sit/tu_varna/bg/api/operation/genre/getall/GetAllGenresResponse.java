package sit.tu_varna.bg.api.operation.genre.getall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.GenreDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAllGenresResponse implements ServiceResponse {
    private Collection<GenreDto> genres;
}
