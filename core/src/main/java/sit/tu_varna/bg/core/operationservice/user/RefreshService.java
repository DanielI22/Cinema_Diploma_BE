package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.keycloak.representations.AccessTokenResponse;
import sit.tu_varna.bg.api.operation.user.refresh.RefreshOperation;
import sit.tu_varna.bg.api.operation.user.refresh.RefreshRequest;
import sit.tu_varna.bg.api.operation.user.refresh.RefreshResponse;
import sit.tu_varna.bg.core.common.KeycloakService;

@ApplicationScoped
public class RefreshService implements RefreshOperation {
    @Inject
    KeycloakService keycloakService;

    @Override
    public RefreshResponse process(RefreshRequest request) {
        AccessTokenResponse accessTokenResponse = keycloakService.refreshToken(request.getRefreshToken());
        return RefreshResponse.builder()
                .accessToken(accessTokenResponse.getToken())
                .refreshToken(accessTokenResponse.getRefreshToken())
                .build();
    }
}
