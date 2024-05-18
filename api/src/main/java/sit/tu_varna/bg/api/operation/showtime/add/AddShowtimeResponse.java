package sit.tu_varna.bg.api.operation.showtime.add;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddShowtimeResponse implements ServiceResponse {
    private Collection<String> showtimeIds;
}
