package sit.tu_varna.bg.core.externalservice;

import jakarta.enterprise.context.ApplicationScoped;
import sit.tu_varna.bg.api.dto.PurchaseSeatDto;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.core.interfaces.PricingOperation;
import sit.tu_varna.bg.enums.TicketType;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Locale;

@ApplicationScoped
public class PricingService implements PricingOperation {

    @Override
    public BigDecimal calculateTicketPrice(TicketType ticketType, BigDecimal basePrice) {
        if (TicketType.NORMAL.equals(ticketType)) {
            return basePrice;
        }
        if (TicketType.REDUCED.equals(ticketType)) {
            return basePrice.multiply(BigDecimal.valueOf(BusinessConstants.REDUCED_PERCENTAGE));
        } else return basePrice;
    }

    @Override
    public BigDecimal calculateOrderPrice(Collection<PurchaseSeatDto> seats, BigDecimal basePrice) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (PurchaseSeatDto seat : seats) {
            TicketType ticketType = TicketType.valueOf(seat.getTicketType().name().toUpperCase(Locale.ROOT));
            BigDecimal seatPrice = calculateTicketPrice(ticketType, basePrice);
            totalPrice = totalPrice.add(seatPrice);
        }
        return totalPrice;
    }
}
