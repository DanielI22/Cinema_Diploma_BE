package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.user.forgotpassword.ForgotPasswordOperation;
import sit.tu_varna.bg.api.operation.user.forgotpassword.ForgotPasswordRequest;
import sit.tu_varna.bg.api.operation.user.forgotpassword.ForgotPasswrodResponse;
import sit.tu_varna.bg.core.common.KeycloakService;
import sit.tu_varna.bg.entity.User;

@ApplicationScoped
public class ForgotPasswordService implements ForgotPasswordOperation {
    @Inject
    KeycloakService keycloakService;

    @Override
    public ForgotPasswrodResponse process(ForgotPasswordRequest request) {
        String userMail = request.getEmail();
        User user = User.findByEmail(userMail)
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + userMail + " not found"));
        keycloakService.sendForgotPassword(user.getId().toString());

        return ForgotPasswrodResponse.builder().sent(true).build();
    }
}
