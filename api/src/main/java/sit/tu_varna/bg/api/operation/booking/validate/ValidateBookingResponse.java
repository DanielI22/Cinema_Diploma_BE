package sit.tu_varna.bg.api.operation.booking.validate;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;
import sit.tu_varna.bg.api.dto.BookingDto;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ValidateBookingResponse implements ServiceResponse {
    private BookingDto booking;
}
