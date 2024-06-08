package sit.tu_varna.bg.core.common;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@QuarkusTest
public class KeycloakServiceTest {

    @InjectMocks
    KeycloakService keycloakService;

    @Mock
    Keycloak keycloakAdmin;

    @Mock
    RealmResource realmResource;

    @Mock
    UsersResource usersResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        keycloakService.serverUrl = "http://localhost:8080";
        keycloakService.clientId = "test-client";
        keycloakService.clientSecret = "test-secret";
        keycloakService.realmName = "test-realm";
        keycloakService.adminUsername = "admin";
        keycloakService.adminPassword = "admin-password";
    }

    @Test
    void testCreateUser_ExceptionHandling() {
        // Arrange
        when(keycloakAdmin.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        when(usersResource.create(any())).thenReturn(mock(Response.class));

        // Simulate failure in user creation
        doThrow(new WebApplicationException("Failed to create user", 500)).when(usersResource).create(any());

        // Act & Assert
        assertThrows(WebApplicationException.class, () -> keycloakService.createUser("username", "email@example.com", "password", Collections.emptyList(), true));
    }

    @Test
    void testLogoutUser_ExceptionHandling() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        when(keycloakAdmin.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        doThrow(new WebApplicationException("Failed to logout user", 500))
                .when(usersResource).get(userId);

        // Act & Assert
        assertThrows(WebApplicationException.class, () -> keycloakService.logoutUser(userId));
    }

    @Test
    void testGetUserRepresentation_ExceptionHandling() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        when(keycloakAdmin.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        doThrow(new WebApplicationException("Failed to fetch user", 404))
                .when(usersResource).get(userId);

        // Act & Assert
        assertThrows(WebApplicationException.class, () -> keycloakService.getUserRepresentation(userId));
    }

    @Test
    void testDeleteUser_ExceptionHandling() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        when(keycloakAdmin.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        doThrow(new WebApplicationException("Failed to delete user", 404))
                .when(usersResource).get(userId);

        // Act & Assert
        assertThrows(WebApplicationException.class, () -> keycloakService.deleteUser(userId));
    }

    @Test
    void testUpdateUsername_ExceptionHandling() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        when(keycloakAdmin.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        doThrow(new WebApplicationException("User with this username already exists", 409))
                .when(usersResource).get(userId);

        // Act & Assert
        assertThrows(WebApplicationException.class, () -> keycloakService.updateUsername(userId, "newUsername"));
    }

    @Test
    void testUpdatePassword_ExceptionHandling() {
        // Arrange
        String userId = UUID.randomUUID().toString();
        when(keycloakAdmin.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
        doThrow(new WebApplicationException("Failed to update", 500))
                .when(usersResource).get(userId);

        // Act & Assert
        assertThrows(WebApplicationException.class, () -> keycloakService.updatePassword(userId, "newPassword"));
    }
}
