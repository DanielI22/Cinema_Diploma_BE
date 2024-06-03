package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.operation.user.register.RegisterOperation;
import sit.tu_varna.bg.api.operation.user.register.RegisterRequest;
import sit.tu_varna.bg.api.operation.user.register.RegisterResponse;
import sit.tu_varna.bg.core.common.KeycloakService;
import sit.tu_varna.bg.entity.User;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

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
        String userId = keycloakService.createUser(username, email, password, roles, false);
        if (Objects.nonNull(userId)) {
            User user = User.builder()
                    .id(UUID.fromString(userId))
                    .email(email)
                    .build();
            User.persist(user);
        }

        return RegisterResponse.builder()
                .created(true)
                .build();
    }
}
