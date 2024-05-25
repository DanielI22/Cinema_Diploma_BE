package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.keycloak.representations.AccessTokenResponse;
import sit.tu_varna.bg.api.operation.user.login.LoginOperation;
import sit.tu_varna.bg.api.operation.user.login.LoginRequest;
import sit.tu_varna.bg.api.operation.user.login.LoginResponse;
import sit.tu_varna.bg.core.common.KeycloakService;

@ApplicationScoped
public class LoginService implements LoginOperation {
    @Inject
    KeycloakService keycloakService;

    @Override
    @Transactional
    public LoginResponse process(LoginRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();

        AccessTokenResponse tokensForUser = keycloakService.getTokensForUser(email, password);
        return LoginResponse.builder()
                .accessToken(tokensForUser.getToken())
                .refreshToken(tokensForUser.getRefreshToken())
                .build();
    }
}
