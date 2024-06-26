package sit.tu_varna.bg.api.operation.booking.getshowtime;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.ShowtimeBookingDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetShowtimeBookingsResponse implements ServiceResponse {
    private Collection<ShowtimeBookingDto> bookings;
}
