package sit.tu_varna.bg.core.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.UserDto;
import sit.tu_varna.bg.core.common.KeycloakService;
import sit.tu_varna.bg.core.constants.BusinessConstants;
import sit.tu_varna.bg.entity.User;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class UserMapperTest {

    @InjectMocks
    UserMapper userMapper;

    @Mock
    KeycloakService keycloakService;

    User user;
    UserRepresentation userRepresentation;
    RoleRepresentation roleRepresentation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        UUID userId = UUID.randomUUID();

        user = User.builder()
                .id(userId)
                .build();

        userRepresentation = new UserRepresentation();
        userRepresentation.setId(userId.toString());
        userRepresentation.setEmail("user@example.com");
        userRepresentation.setUsername("username");

        roleRepresentation = new RoleRepresentation();
        roleRepresentation.setName("ROLE_USER");

        when(keycloakService.getUserRepresentation(userId.toString())).thenReturn(userRepresentation);
        when(keycloakService.getUserRoles(userId.toString())).thenReturn(Collections.singletonList(roleRepresentation));
    }

    @Test
    void testUserToUserDto() {
        UserDto userDto = userMapper.userToUserDto(user);

        assertEquals(user.getId().toString(), userDto.getId());
        assertEquals(userRepresentation.getEmail(), userDto.getEmail());
        assertEquals(userRepresentation.getUsername(), userDto.getName());
        assertEquals(roleRepresentation.getName(), userDto.getRole());
    }

    @Test
    void testUserToUserDtoWithDefaultRole() {
        roleRepresentation.setName(BusinessConstants.KEYCLOAK_DEFAULT_ROLE);
        when(keycloakService.getUserRoles(user.getId().toString())).thenReturn(Collections.singletonList(roleRepresentation));

        UserDto userDto = userMapper.userToUserDto(user);

        assertEquals(user.getId().toString(), userDto.getId());
        assertEquals(userRepresentation.getEmail(), userDto.getEmail());
        assertEquals(userRepresentation.getUsername(), userDto.getName());
        assertEquals(BusinessConstants.USER_ROLE, userDto.getRole());
    }
}
