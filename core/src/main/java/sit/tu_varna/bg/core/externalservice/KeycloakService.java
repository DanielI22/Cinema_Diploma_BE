package sit.tu_varna.bg.core.externalservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.RolesResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@ApplicationScoped
public class KeycloakService {
    @ConfigProperty(name = "keycloak.server-url")
    String serverUrl;

    @ConfigProperty(name = "quarkus.oidc.client-id")
    String clientId;

    @ConfigProperty(name = "quarkus.oidc.credentials.secret")
    String clientSecret;

    @ConfigProperty(name = "keycloak.realm-name")
    String realmName;

    @ConfigProperty(name = "keycloak.admin.username")
    String adminUsername;

    @ConfigProperty(name = "keycloak.admin.password")
    String adminPassword;

    @SuppressWarnings("all")
    @Inject
    ObjectMapper objectMapper;

    public String createUser(String username, String email, String password, List<String> roles) {
        try (Keycloak keycloakAdmin = getKeycloakAdmin()) {
            final RealmResource realmResource = keycloakAdmin.realm(realmName);
            final UsersResource usersResource = realmResource.users();

            UserRepresentation newUser = createUserRepresentation(username, email);
            String userId = createUserAndGetId(usersResource, newUser);

            List<RoleRepresentation> roleRepresentations = getOrCreateRoles(roles, realmResource);
            setUserPasswordAndRoles(usersResource, userId, password, roleRepresentations);

            return userId;
        }
    }

    public AccessTokenResponse getTokensForUser(String username, String password) {
        try (Keycloak keycloakUser = getKeycloakUser(username, password)) {
            try {
                return keycloakUser.tokenManager().getAccessToken();
            } catch (Exception e) {
                throw new WebApplicationException(Response.status(404).entity("Bad credentials").build());
            }
        }
    }

    public void logoutUser(String userId) {
        try (Keycloak keycloakAdmin = getKeycloakAdmin()) {
            final RealmResource realmResource = keycloakAdmin.realm(realmName);
            try {
                final UserResource userResource = realmResource.users().get(userId);
                if (Objects.nonNull(userResource)) {
                    userResource.logout();
                }
            } catch (NotFoundException e) {
                throw new WebApplicationException("Failed to logout user: " + e.getMessage(), e.getResponse().getStatus());
            }
        }
    }

    public AccessTokenResponse refreshToken(String refreshToken) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(serverUrl + "/realms/" + realmName + "/protocol/openid-connect/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            "client_id=" + clientId +
                                    "&client_secret=" + clientSecret +
                                    "&grant_type=refresh_token" +
                                    "&refresh_token=" + refreshToken))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), AccessTokenResponse.class);
            } else {
                throw new WebApplicationException(Response.status(500).entity("Failed to refresh").build());
            }
        } catch (Exception e) {
            throw new WebApplicationException(Response.status(500).entity("Failed to refresh").build());
        }
    }

    private UserRepresentation createUserRepresentation(String username, String email) {
        UserRepresentation newUser = new UserRepresentation();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setEnabled(true);
        newUser.setEmailVerified(true);
        return newUser;
    }

    private String createUserAndGetId(UsersResource usersResource, UserRepresentation newUser) {
        Response response = usersResource.create(newUser);
        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            response.close();
            throw new WebApplicationException("Failed to create user", response.getStatus());
        }
        String userId = CreatedResponseUtil.getCreatedId(response);
        response.close();
        return userId;
    }

    private void setUserPasswordAndRoles(UsersResource usersResource, String userId, String password, List<RoleRepresentation> roles) {
        UserResource userResource = usersResource.get(userId);
        CredentialRepresentation passwordCred = createPasswordCredentials(password);
        userResource.resetPassword(passwordCred);
        userResource.roles().realmLevel().add(roles);
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);
        return passwordCred;
    }

    private List<RoleRepresentation> getOrCreateRoles(List<String> roles, RealmResource realmResource) {
        return roles.stream()
                .map(role -> getOrCreateRole(realmResource, role))
                .collect(Collectors.toList());
    }

    private RoleRepresentation getOrCreateRole(RealmResource realmResource, String roleName) {
        RolesResource rolesResource = realmResource.roles();
        try {
            return rolesResource.get(roleName).toRepresentation();
        } catch (NotFoundException e) {
            RoleRepresentation newRole = new RoleRepresentation();
            newRole.setName(roleName);
            newRole.setClientRole(false);
            rolesResource.create(newRole);
            return rolesResource.get(roleName).toRepresentation();
        }
    }

    private Keycloak getKeycloakAdmin() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .clientId("admin-cli")
                .grantType(OAuth2Constants.PASSWORD)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    private Keycloak getKeycloakUser(String username, String password) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realmName)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .build();
    }
}
