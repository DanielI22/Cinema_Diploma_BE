package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.user.delete.DeleteUserOperation;
import sit.tu_varna.bg.api.operation.user.delete.DeleteUserRequest;
import sit.tu_varna.bg.api.operation.user.delete.DeleteUserResponse;
import sit.tu_varna.bg.core.common.KeycloakService;
import sit.tu_varna.bg.entity.Booking;
import sit.tu_varna.bg.entity.Ticket;
import sit.tu_varna.bg.entity.User;
import sit.tu_varna.bg.enums.BookingStatus;

import java.util.Collection;
import java.util.UUID;

@ApplicationScoped
public class DeleteUserService implements DeleteUserOperation {
    @Inject
    KeycloakService keycloakService;


    @Override
    @Transactional
    public DeleteUserResponse process(DeleteUserRequest request) {
        UUID userId = request.getUserId();
        User user = (User) User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with id " + userId + " not found"));
        if (user.isPersistent()) {
            user.getBookings().forEach(booking -> booking.setStatus(BookingStatus.CANCELLED));
            user.getBookings().stream().
                    map(Booking::getTickets)
                    .flatMap(Collection::stream)
                    .map(Ticket::getShowtimeSeat)
                    .forEach(showtimeSeat -> {
                        showtimeSeat.setBooked(false);
                        showtimeSeat.persist();
                    });
            user.delete();
        }
        keycloakService.deleteUser(userId.toString());
        return DeleteUserResponse.builder().deleted(true).build();
    }
}
