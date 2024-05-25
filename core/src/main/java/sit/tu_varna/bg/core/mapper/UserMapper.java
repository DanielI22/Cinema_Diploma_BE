package sit.tu_varna.bg.core.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import sit.tu_varna.bg.api.dto.UserDto;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.core.common.KeycloakService;
import sit.tu_varna.bg.core.interfaces.ObjectMapper;
import sit.tu_varna.bg.entity.User;

import java.util.Collection;

@ApplicationScoped
public class UserMapper implements ObjectMapper {
    @Inject
    KeycloakService keycloakService;

    public UserDto userToUserDto(User user) {
        String userId = user.getId().toString();
        UserRepresentation userRepresentation = keycloakService.getUserRepresentation(userId);
        Collection<RoleRepresentation> roles = keycloakService.getUserRoles(userId);

        return UserDto.builder()
                .id(userId)
                .email(userRepresentation.getEmail())
                .name(userRepresentation.getUsername())
                .role(roles.stream()
                        .map(RoleRepresentation::getName)
                        .filter(name -> !name.equals(BusinessConstants.KEYCLOAK_DEFAULT_ROLE))
                        .findFirst()
                        .orElse(BusinessConstants.USER_ROLE))
                .build();
    }
}
