package sit.tu_varna.bg.core.operationservice.booking;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.dto.PurchaseSeatDto;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.booking.book.BookingOperation;
import sit.tu_varna.bg.api.operation.booking.book.BookingRequest;
import sit.tu_varna.bg.api.operation.booking.book.BookingResponse;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.core.externalservice.PricingService;
import sit.tu_varna.bg.entity.*;
import sit.tu_varna.bg.enums.BookingStatus;
import sit.tu_varna.bg.enums.TicketStatus;
import sit.tu_varna.bg.enums.TicketType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.UUID;

@ApplicationScoped
public class BookingService implements BookingOperation {
    @Inject
    PricingService pricingService;

    @Override
    @Transactional
    public BookingResponse process(BookingRequest request) {
        UUID userId = request.getUserId();
        User user = (User) User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        UUID showtimeId = request.getOrderInfo().getShowtimeId();
        Showtime showtime = (Showtime) Showtime.findByIdOptional(showtimeId)
                .orElseThrow(() -> new ResourceNotFoundException("Showtime with id " + showtimeId + " not found"));

        if (Duration.between(LocalDateTime.now(), showtime.getStartTime()).toMinutes() <= BusinessConstants.BOOKING_EXPIRE_TIME) {
            throw new ResourceAlreadyExistsException("Showtime has already started");
        }

        Booking booking = Booking.builder()
                .user(user)
                .showtime(showtime)
                .status(BookingStatus.AVAILABLE)
                .build();

        booking.persist();
        for (PurchaseSeatDto bookingSeat : request.getOrderInfo().getSeats()) {
            ShowtimeSeat showtimeSeat = ShowtimeSeat.findBySeatIdAndShowtimeId(bookingSeat.getSeatId(), showtimeId)
                    .stream()
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Invalid showtime seat"));

            if (showtimeSeat.isBooked()) {
                throw new ResourceAlreadyExistsException("Seat already booked");
            }

            showtimeSeat.setBooked(true);
            showtimeSeat.persist();

            TicketType ticketType = TicketType.valueOf(bookingSeat.getTicketType().name().toUpperCase(Locale.ROOT));
            Ticket ticket = Ticket.builder()
                    .booking(booking)
                    .user(user)
                    .showtimeSeat(showtimeSeat)
                    .showtime(showtime)
                    .ticketType(ticketType)
                    .price(pricingService.calculateTicketPrice(ticketType, showtime.getTicketPrice()))
                    .ticketStatus(TicketStatus.BOOKED)
                    .build();

            ticket.persist();
            booking.getTickets().add(ticket);
        }

        return BookingResponse.builder()
                .bookingId(booking.getId().toString())
                .build();
    }
}
