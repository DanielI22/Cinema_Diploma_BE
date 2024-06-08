package sit.tu_varna.bg.core.common;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sit.tu_varna.bg.api.dto.PurchaseSeatDto;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.enums.TicketType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PricingServiceTest {

    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingService();
    }

    @Test
    void testCalculateOrderPriceWithNormalAndReducedTickets() {
        PurchaseSeatDto normalSeat = new PurchaseSeatDto(UUID.randomUUID(), PurchaseSeatDto.TicketType.normal);
        PurchaseSeatDto reducedSeat = new PurchaseSeatDto(UUID.randomUUID(), PurchaseSeatDto.TicketType.reduced);
        List<PurchaseSeatDto> seats = Arrays.asList(normalSeat, reducedSeat);

        BigDecimal basePrice = new BigDecimal("10.00");

        BigDecimal expectedPrice = basePrice.add(basePrice.multiply(BigDecimal.valueOf(BusinessConstants.REDUCED_PERCENTAGE)));

        BigDecimal result = pricingService.calculateOrderPrice(seats, basePrice);

        assertEquals(expectedPrice, result);
    }

    @Test
    void testCalculateOrderPriceWithOnlyNormalTickets() {
        PurchaseSeatDto normalSeat = new PurchaseSeatDto(UUID.randomUUID(), PurchaseSeatDto.TicketType.normal);
        List<PurchaseSeatDto> seats = Collections.singletonList(normalSeat);

        BigDecimal basePrice = new BigDecimal("10.00");

        BigDecimal result = pricingService.calculateOrderPrice(seats, basePrice);

        assertEquals(basePrice, result);
    }

    @Test
    void testCalculateOrderPriceWithOnlyReducedTickets() {
        PurchaseSeatDto reducedSeat = new PurchaseSeatDto(UUID.randomUUID(), PurchaseSeatDto.TicketType.reduced);
        List<PurchaseSeatDto> seats = Collections.singletonList(reducedSeat);

        BigDecimal basePrice = new BigDecimal("10.00");

        BigDecimal expectedPrice = basePrice.multiply(BigDecimal.valueOf(BusinessConstants.REDUCED_PERCENTAGE));

        BigDecimal result = pricingService.calculateOrderPrice(seats, basePrice);

        assertEquals(expectedPrice, result);
    }

    @Test
    void testCalculateTicketPriceForNormalTicket() {
        BigDecimal basePrice = new BigDecimal("10.00");
        BigDecimal result = pricingService.calculateTicketPrice(TicketType.NORMAL, basePrice);

        assertEquals(basePrice, result);
    }

    @Test
    void testCalculateTicketPriceForReducedTicket() {
        BigDecimal basePrice = new BigDecimal("10.00");
        BigDecimal expectedPrice = basePrice.multiply(BigDecimal.valueOf(BusinessConstants.REDUCED_PERCENTAGE));

        BigDecimal result = pricingService.calculateTicketPrice(TicketType.REDUCED, basePrice);

        assertEquals(expectedPrice, result);
    }

    @Test
    void testCalculateTicketPriceForUnknownTicketType() {
        BigDecimal basePrice = new BigDecimal("10.00");
        BigDecimal result = pricingService.calculateTicketPrice(TicketType.NORMAL, basePrice);

        assertEquals(basePrice, result); // Default to base price
    }
}
