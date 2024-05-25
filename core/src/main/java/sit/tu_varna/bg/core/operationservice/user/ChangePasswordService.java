package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.user.changepassword.ChangePasswordOperation;
import sit.tu_varna.bg.api.operation.user.changepassword.ChangePasswordRequest;
import sit.tu_varna.bg.api.operation.user.changepassword.ChangePasswordResponse;
import sit.tu_varna.bg.core.common.KeycloakService;
import sit.tu_varna.bg.entity.User;

import java.util.UUID;

@ApplicationScoped
public class ChangePasswordService implements ChangePasswordOperation {
    @Inject
    KeycloakService keycloakService;

    @Override
    public ChangePasswordResponse process(ChangePasswordRequest request) {
        UUID userId = request.getUserId();
        User.findByIdOptional(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        keycloakService.updatePassword(userId.toString(), request.getPassword());

        return ChangePasswordResponse.builder().userId(userId.toString()).build();
    }
}
