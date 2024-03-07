package sit.tu_varna.bg.core.service.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.operation.user.register.RegisterOperation;
import sit.tu_varna.bg.api.operation.user.register.RegisterRequest;
import sit.tu_varna.bg.api.operation.user.register.RegisterResponse;
import sit.tu_varna.bg.data.entity.User;
import sit.tu_varna.bg.data.enums.UserRole;
import sit.tu_varna.bg.data.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterOperation {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public RegisterResponse process(RegisterRequest request) {
        userRepository
                .findByUsername(request.getUsername())
                .ifPresent(e -> {
                    throw new ResourceAlreadyExistsException("User already exists");
                });

        userRepository
                .findByEmail(request.getEmail())
                .ifPresent(e -> {
                    throw new ResourceAlreadyExistsException("Email already exists");
                });

        User user = User
                .builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .isArchived(false)
                .build();

        userRepository.save(user);

        return RegisterResponse.builder()
                .userId(user.getId().toString())
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}
