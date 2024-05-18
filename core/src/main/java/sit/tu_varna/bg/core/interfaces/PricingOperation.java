package sit.tu_varna.bg.core.interfaces;

import sit.tu_varna.bg.api.dto.PurchaseSeatDto;
import sit.tu_varna.bg.enums.TicketType;

import java.math.BigDecimal;
import java.util.Collection;

public interface PricingOperation {
    BigDecimal calculateTicketPrice(TicketType ticketType, BigDecimal basePrice);

    BigDecimal calculateOrderPrice(Collection<PurchaseSeatDto> seats, BigDecimal basePrice);
}
