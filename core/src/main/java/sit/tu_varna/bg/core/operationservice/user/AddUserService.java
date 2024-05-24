package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import sit.tu_varna.bg.api.operation.user.add.AddUserOperation;
import sit.tu_varna.bg.api.operation.user.add.AddUserRequest;
import sit.tu_varna.bg.api.operation.user.add.AddUserResponse;
import sit.tu_varna.bg.core.externalservice.KeycloakService;
import sit.tu_varna.bg.entity.User;

import java.util.*;

@ApplicationScoped
public class AddUserService implements AddUserOperation {
    @Inject
    KeycloakService keycloakService;

    @Override
    @Transactional
    public AddUserResponse process(AddUserRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();
        String email = request.getEmail();
        String role = request.getRole();

        List<String> roles = Objects.nonNull(role) ? Collections.singletonList(role) : Collections.emptyList();
        String userId = keycloakService.createUser(username, email, password, roles);
        if (Objects.nonNull(userId)) {
            User user = User.builder()
                    .id(UUID.fromString(userId))
                    .email(email)
                    .build();
            User.persist(user);
        }

        return AddUserResponse.builder()
                .userId(userId)
                .build();
    }
}
