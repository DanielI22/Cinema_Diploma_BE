package sit.tu_varna.bg.core.service.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.operation.user.register.RegisterOperation;
import sit.tu_varna.bg.api.operation.user.register.RegisterRequest;
import sit.tu_varna.bg.api.operation.user.register.RegisterResponse;
import sit.tu_varna.bg.core.service.keycloak.KeycloakAdminService;
import sit.tu_varna.bg.entity.User;

import java.util.Objects;
import java.util.UUID;

@ApplicationScoped
public class RegisterService implements RegisterOperation {
    @Inject
    KeycloakAdminService keycloakAdminService;


    @Override
    @Transactional
    public RegisterResponse process(RegisterRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();

        String userId = keycloakAdminService.createUser(username, email, password);
        if (Objects.nonNull(userId)) {
            User user = User.builder()
                    .id(UUID.fromString(userId))
                    .build();
            User.persist(user);
        }
//        AccessTokenResponse tokensForUser = keycloakAdminService.getTokensForUser(username, password);
        return RegisterResponse.builder()
//                .accessToken(tokensForUser.getToken())
//                .refreshToken(tokensForUser.getRefreshToken())
                .build();
    }
}
