package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.operation.user.logout.LogoutOperation;
import sit.tu_varna.bg.api.operation.user.logout.LogoutRequest;
import sit.tu_varna.bg.api.operation.user.logout.LogoutResponse;
import sit.tu_varna.bg.core.common.KeycloakService;

@ApplicationScoped
public class LogoutService implements LogoutOperation {
    @Inject
    KeycloakService keycloakService;

    @Override
    public LogoutResponse process(LogoutRequest request) {
        keycloakService.logoutUser(request.getUserId());
        return LogoutResponse.builder().success(true).build();
    }
}
