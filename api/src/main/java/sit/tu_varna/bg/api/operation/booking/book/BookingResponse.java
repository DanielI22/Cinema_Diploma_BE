package sit.tu_varna.bg.api.operation.booking.book;

import lombok.*;
import sit.tu_varna.bg.api.base.ServiceResponse;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse implements ServiceResponse {
    private String bookingId;
}
