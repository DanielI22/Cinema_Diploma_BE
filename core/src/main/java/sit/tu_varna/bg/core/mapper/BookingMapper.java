package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.BookingDto;
import sit.tu_varna.bg.api.dto.ShowtimeBookingDto;
import sit.tu_varna.bg.core.interfaces.ObjectMapper;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.entity.Ticket;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.stream.Collectors;

@ApplicationScoped
public class BookingMapper implements ObjectMapper {
    @Inject
    TicketMapper ticketMapper;

    public BookingDto bookingToBookingDto(Booking booking) {
        return BookingDto.builder()
                .id(booking.getId().toString())
                .shortcode(booking.getShortCode())
                .moviePoster(booking.getShowtime().getMovie().getPosterImageUrl())
                .movieTitle(booking.getShowtime().getMovie().getTitle())
                .cinemaName(booking.getShowtime().getCinema().getName())
                .showtimeStartTime(booking.getShowtime().getStartTime())
                .status(booking.getStatus().name().toLowerCase(Locale.ROOT))
                .tickets(booking.getTickets().stream().map(ticketMapper::ticketToTicketDto).collect(Collectors.toList()))
                .totalPrice(booking.getTickets().stream().map(Ticket::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add).doubleValue())
                .build();
    }

    public ShowtimeBookingDto bookingToShowtimeBookingDto(Booking booking) {
        return ShowtimeBookingDto.builder()
                .id(booking.getId().toString())
                .userMail(booking.getUser().getEmail())
                .status(booking.getStatus().name().toLowerCase(Locale.ROOT))
                .tickets(booking.getTickets().stream().map(ticketMapper::ticketToTicketDto).collect(Collectors.toList()))
                .build();
    }
}
