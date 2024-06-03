package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.user.resend.ResendVerificationOperation;
import sit.tu_varna.bg.api.operation.user.resend.ResendVerificationRequest;
import sit.tu_varna.bg.api.operation.user.resend.ResendVerificationResponse;
import sit.tu_varna.bg.core.common.KeycloakService;
import sit.tu_varna.bg.entity.User;

@ApplicationScoped
public class ResendVerificationService implements ResendVerificationOperation {
    @Inject
    KeycloakService keycloakService;

    @Override
    public ResendVerificationResponse process(ResendVerificationRequest request) {
        String userMail = request.getEmail();
        User user = User.findByEmail(userMail)
                .orElseThrow(() -> new ResourceNotFoundException("User does not exist"));
        keycloakService.sendVerificationMail(user.getId().toString());

        return ResendVerificationResponse.builder().sent(true).build();
    }
}
