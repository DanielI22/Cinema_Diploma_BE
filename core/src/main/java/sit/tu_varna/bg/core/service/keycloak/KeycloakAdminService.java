package sit.tu_varna.bg.core.service.keycloak;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

@ApplicationScoped
public class KeycloakAdminService {
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

    public String createUser(String username, String email, String password) {
        Keycloak keycloak = getKeycloakAdmin();

        UserRepresentation newUser = new UserRepresentation();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setEnabled(true);
        newUser.setEmailVerified(true);
        RealmResource realmResource = keycloak.realm(realmName);
        UsersResource usersResource = realmResource.users();

        Response response = usersResource.create(newUser);
        if (response.getStatus() != Response.Status.CREATED.getStatusCode()) {
            throw new RuntimeException("Failed to create user: " + response.getStatusInfo().getReasonPhrase());
        }
        String userId = CreatedResponseUtil.getCreatedId(response);

        CredentialRepresentation passwordCred = new CredentialRepresentation();
        passwordCred.setTemporary(false);
        passwordCred.setType(CredentialRepresentation.PASSWORD);
        passwordCred.setValue(password);

        UserResource userResource = usersResource.get(userId);

        userResource.resetPassword(passwordCred);
        keycloak.close();

        return userId;
    }

    public AccessTokenResponse getTokensForUser(String username, String password) {
        Keycloak keycloak = getKeycloakUser(username, password);

        AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();
        keycloak.close();
        return tokenResponse;
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
