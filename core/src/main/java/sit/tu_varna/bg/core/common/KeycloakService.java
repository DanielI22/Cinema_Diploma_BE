package sit.tu_varna.bg.core.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.reactive.ClientWebApplicationException;
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
import java.util.Collection;
import java.util.Collections;
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

    public String createUser(String username, String email, String password, List<String> roles, boolean verified) {
        try (Keycloak keycloakAdmin = getKeycloakAdmin()) {
            final RealmResource realmResource = keycloakAdmin.realm(realmName);
            final UsersResource usersResource = realmResource.users();

            UserRepresentation newUser = createUserRepresentation(username, email, verified);
            String userId = createUserAndGetId(usersResource, newUser);

            if (!verified) {
                sendVerificationMail(userId);
            }
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
                if (e instanceof ClientWebApplicationException ce) {
                    if (ce.getResponse().getStatus() == 401) {
                        throw new WebApplicationException(Response.status(404).entity("Bad credentials").build());
                    } else if (ce.getResponse().getStatus() == 400) {
                        throw new WebApplicationException(Response.status(401).entity("Unverified email").build());
                    }
                } else {
                    throw new WebApplicationException(Response.status(500).entity("Server error").build());
                }
            }
            throw new WebApplicationException(Response.status(500).entity("Server error").build());
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
                throw new WebApplicationException("Invalid refresh token", Response.Status.BAD_REQUEST);
            }
        } catch (Exception e) {
            if (e instanceof WebApplicationException) {
                throw new WebApplicationException("Invalid refresh token", Response.Status.BAD_REQUEST);
            } else {
                throw new WebApplicationException(Response.status(500).entity("Failed to refresh").build());
            }
        }
    }

    public UserRepresentation getUserRepresentation(String userId) {
        try (Keycloak keycloakAdmin = getKeycloakAdmin()) {
            final RealmResource realmResource = keycloakAdmin.realm(realmName);
            try {
                final UserResource userResource = realmResource.users().get(userId);
                return userResource.toRepresentation();
            } catch (NotFoundException e) {
                throw new WebApplicationException("Failed to fetch user: " + e.getMessage(), e.getResponse().getStatus());
            }
        }
    }

    public Collection<RoleRepresentation> getUserRoles(String userId) {
        try (Keycloak keycloakAdmin = getKeycloakAdmin()) {
            final RealmResource realmResource = keycloakAdmin.realm(realmName);
            try {
                final UserResource userResource = realmResource.users().get(userId);
                return userResource.roles().realmLevel().listAll();
            } catch (NotFoundException e) {
                throw new WebApplicationException("Failed to fetch user: " + e.getMessage(), e.getResponse().getStatus());
            }
        }
    }

    public void deleteUser(String userId) {
        try (Keycloak keycloakAdmin = getKeycloakAdmin()) {
            try {
                RealmResource realmResource = keycloakAdmin.realm(realmName);
                UserResource userResource = realmResource.users().get(userId);
                userResource.remove();
            } catch (NotFoundException ignored) {
            }
        }
    }

    public void updateUsername(String userId, String newUsername) {
        try (Keycloak keycloakAdmin = getKeycloakAdmin()) {
            RealmResource realmResource = keycloakAdmin.realm(realmName);
            try {
                UserResource userResource = realmResource.users().get(userId);
                UserRepresentation userRepresentation = userResource.toRepresentation();
                userRepresentation.setUsername(newUsername);
                userResource.update(userRepresentation);
                userRepresentation.setUsername(newUsername);
                userResource.update(userRepresentation);
            } catch (Exception e) {
                if (e instanceof ClientWebApplicationException) {
                    throw new WebApplicationException("User with this username already exists", Response.Status.CONFLICT);
                } else {
                    throw new WebApplicationException(Response.status(500).entity("Failed to update").build());
                }
            }
        }
    }

    public void updatePassword(String userId, String newPassword) {
        try (Keycloak keycloakAdmin = getKeycloakAdmin()) {
            RealmResource realmResource = keycloakAdmin.realm(realmName);
            try {
                UserResource userResource = realmResource.users().get(userId);
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setTemporary(false);
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(newPassword);
                userResource.resetPassword(credential);
            } catch (Exception e) {
                throw new WebApplicationException(Response.status(500).entity("Failed to update").build());
            }
        }
    }

    public void sendVerificationMail(String userId) {
        try (Keycloak keycloakAdmin = getKeycloakAdmin()) {
            final RealmResource realmResource = keycloakAdmin.realm(realmName);
            try {
                final UserResource userResource = realmResource.users().get(userId);
                if (Objects.nonNull(userResource)) {
                    userResource.sendVerifyEmail();
                }
            } catch (NotFoundException e) {
                throw new WebApplicationException(Response.status(500).entity("Failed to send verification email").build());
            }
        }
    }

    public void sendForgotPassword(String userId) {
        try (Keycloak keycloakAdmin = getKeycloakAdmin()) {
            final RealmResource realmResource = keycloakAdmin.realm(realmName);
            try {
                final UserResource userResource = realmResource.users().get(userId);
                if (Objects.nonNull(userResource)) {
                    userResource.executeActionsEmail(Collections.singletonList("UPDATE_PASSWORD"));
                }
            } catch (NotFoundException e) {
                throw new WebApplicationException(Response.status(500).entity("Failed to send forgot password email").build());
            }
        }
    }

    private UserRepresentation createUserRepresentation(String username, String email, boolean verified) {
        UserRepresentation newUser = new UserRepresentation();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setEnabled(true);
        newUser.setEmailVerified(verified);
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
