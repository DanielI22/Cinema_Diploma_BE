package sit.tu_varna.bg.api.operation.cinema.edit;

import jakarta.validation.constraints.NotBlank;
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
    @NotBlank
    private String name;
    @NotBlank
    private String location;
    private String imageUrl;
    private Collection<UUID> halls;
}
