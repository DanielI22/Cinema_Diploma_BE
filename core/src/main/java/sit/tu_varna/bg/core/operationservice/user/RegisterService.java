package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.keycloak.representations.AccessTokenResponse;
import sit.tu_varna.bg.api.operation.user.register.RegisterOperation;
import sit.tu_varna.bg.api.operation.user.register.RegisterRequest;
import sit.tu_varna.bg.api.operation.user.register.RegisterResponse;
import sit.tu_varna.bg.core.externalservice.KeycloakService;
import sit.tu_varna.bg.entity.User;

import java.util.*;

@ApplicationScoped
public class RegisterService implements RegisterOperation {
    @Inject
    KeycloakService keycloakService;

    @Override
    @Transactional
    public RegisterResponse process(RegisterRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        List<String> roles = User.findAll().count() == 0 ? List.of("admin") : Collections.emptyList();
        String userId = keycloakService.createUser(username, email, password, roles);
        if (Objects.nonNull(userId)) {
            User user = User.builder()
                    .id(UUID.fromString(userId))
                    .build();
            User.persist(user);
        }
        AccessTokenResponse tokensForUser = keycloakService.getTokensForUser(username, password);
        return RegisterResponse.builder()
                .accessToken(tokensForUser.getToken())
                .refreshToken(tokensForUser.getRefreshToken())
                .build();
    }
}
