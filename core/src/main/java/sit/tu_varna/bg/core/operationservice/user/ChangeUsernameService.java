package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.user.changeusername.ChangeUsernameOperation;
import sit.tu_varna.bg.api.operation.user.changeusername.ChangeUsernameRequest;
import sit.tu_varna.bg.api.operation.user.changeusername.ChangeUsernameResponse;
import sit.tu_varna.bg.core.externalservice.KeycloakService;
import sit.tu_varna.bg.entity.User;

import java.util.UUID;

@ApplicationScoped
public class ChangeUsernameService implements ChangeUsernameOperation {
    @Inject
    KeycloakService keycloakService;

    @Override
    @Transactional
    public ChangeUsernameResponse process(ChangeUsernameRequest request) {
        UUID userId = request.getUserId();
        User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        keycloakService.updateUsername(userId.toString(), request.getUsername());

        return ChangeUsernameResponse.builder().userId(userId.toString()).build();
    }
}
