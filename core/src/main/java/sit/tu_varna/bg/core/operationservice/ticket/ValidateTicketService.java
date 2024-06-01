package sit.tu_varna.bg.core.operationservice.ticket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.exception.InvalidResourceException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.ticket.validate.ValidateTicketOperation;
import sit.tu_varna.bg.api.operation.ticket.validate.ValidateTicketRequest;
import sit.tu_varna.bg.api.operation.ticket.validate.ValidateTicketResponse;
import sit.tu_varna.bg.core.mapper.TicketMapper;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.enums.TicketStatus;

import java.time.LocalDateTime;

import static sit.tu_varna.bg.core.constants.BusinessConstants.TICKET_VALID_BEFORE_SHOWTIME;

@ApplicationScoped
public class ValidateTicketService implements ValidateTicketOperation {
    @Inject
    TicketMapper ticketMapper;

    @Override
    public ValidateTicketResponse process(ValidateTicketRequest request) {
        String shortcode = request.getShortCode();
        Ticket ticket = Ticket.findByShortCode(shortcode)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid ticket code."));


        // Check availability
        if (!ticket.getShowtime().getCinema().getId().equals(request.getCinemaId())) {
            throw new ResourceNotFoundException("Invalid ticket. Wrong cinema");
        }

        if (!ticket.getShowtime().getId().equals(request.getShowtimeId())) {
            throw new ResourceNotFoundException("Invalid ticket. Wrong showtime");
        }
        // Check ticket status
        if (!ticket.getTicketStatus().equals(TicketStatus.PURCHASED)) {
            throw new InvalidResourceException("Invalid ticket. Ticket is not purchased");
        }

        // Check if the showtime has not ended
        LocalDateTime showtimeStart = ticket.getShowtime().getStartTime();
        LocalDateTime showtimeEnd = showtimeStart.plusMinutes(ticket.getShowtime().getMovie().getDuration());
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(showtimeEnd)) {
            throw new InvalidResourceException("Invalid ticket. Showtime has ended");
        }

        // Check if the showtime starts within the next 30 minutes
        if (now.isBefore(showtimeStart.minusMinutes(TICKET_VALID_BEFORE_SHOWTIME))) {
            throw new InvalidResourceException("Invalid ticket. Showtime starts in more than 30 minutes");
        }
        return ValidateTicketResponse.builder().ticket(ticketMapper.ticketToTicketDto(ticket)).build();
    }
}
