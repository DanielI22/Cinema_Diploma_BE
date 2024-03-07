package sit.tu_varna.bg.core.task;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import sit.tu_varna.bg.data.repository.TokenRepository;

@Component
@RequiredArgsConstructor
public class TokenCleanupTask {
    private final TokenRepository tokenRepository;

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void cleanupRevokedTokens() {
        tokenRepository.deleteRevokedTokens();
    }
}
