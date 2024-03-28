package sit.tu_varna.bg.api.operation.cinema.edit;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceRequest;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditCinemaRequest implements ServiceRequest {
    private UUID cinemaId;
    private String name;
    private String location;
    private String imageUrl;
    private Collection<UUID> halls;
}
