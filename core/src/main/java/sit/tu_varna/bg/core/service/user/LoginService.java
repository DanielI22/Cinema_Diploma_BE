package sit.tu_varna.bg.core.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.user.login.LoginOperation;
import sit.tu_varna.bg.api.operation.user.login.LoginRequest;
import sit.tu_varna.bg.api.operation.user.login.LoginResponse;
import sit.tu_varna.bg.data.entity.Token;
import sit.tu_varna.bg.data.entity.User;
import sit.tu_varna.bg.data.enums.TokenType;
import sit.tu_varna.bg.data.repository.TokenRepository;
import sit.tu_varna.bg.data.repository.UserRepository;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoginService implements LoginOperation {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;

    @Override
    @Transactional
    public LoginResponse process(LoginRequest request) {
        String username = request.getUsername();
        String password = request.getPassword();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveToken(user, token);
        updateLastLogonTime(user);

        return LoginResponse
                .builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();

    }

    private void revokeAllUserTokens(User user) {
        List<Token> validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) {
            return;
        }
        validUserTokens.forEach(t -> t.setRevoked(true));
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveToken(User user, String token) {
        Token toPersist = Token
                .builder()
                .user(user)
                .token(token)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .build();
        tokenRepository.save(toPersist);
    }

    private void updateLastLogonTime(User user) {
        user.setLastLoginTime(Timestamp.from(Instant.now()));
        userRepository.save(user);
    }
}
