package sit.tu_varna.bg.api.operation.cinema.add;

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
public class AddCinemaRequest implements ServiceRequest {
    @NotBlank(message = "Name is required.")
    private String name;
    @NotBlank(message = "Location is required.")
    private String location;
    private String imageUrl;
    private Collection<UUID> halls;
}
