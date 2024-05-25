package sit.tu_varna.bg.api.operation.booking.getmybookings;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.BookingDto;

import java.util.Collection;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetMyBookingsResponse implements ServiceResponse {
    private Collection<BookingDto> bookings;
}
