package sit.tu_varna.bg.api.operation.favourite.getall;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.MovieDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetFavouritesResponse implements ServiceResponse {
    private Collection<MovieDto> movies;
}
