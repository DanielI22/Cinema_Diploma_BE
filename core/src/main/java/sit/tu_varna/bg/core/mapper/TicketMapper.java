package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.TicketDto;
import sit.tu_varna.bg.core.interfaces.ObjectMapper;
import sit.tu_varna.bg.entity.Ticket;

import java.util.Locale;

@ApplicationScoped
public class TicketMapper implements ObjectMapper {
    @Inject
    HallMapper hallMapper;

    public TicketDto ticketToTicketDto(Ticket ticket) {
        return TicketDto.builder()
                .id(ticket.getId().toString())
                .userMail(ticket.getUser().getEmail())
                .type(ticket.getTicketType().name().toLowerCase(Locale.ROOT))
                .price(ticket.getPrice().doubleValue())
                .seat(hallMapper.seatToSeatDto(ticket.getShowtimeSeat().getSeat()))
                .build();
    }
}
