package sit.tu_varna.bg.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.tu_varna.bg.api.exception.ResourceAlreadyExistsException;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.user.password.PasswordChangeOperation;
import sit.tu_varna.bg.api.operation.user.password.PasswordChangeRequest;
import sit.tu_varna.bg.api.operation.user.password.PasswordChangeResponse;
import sit.tu_varna.bg.data.entity.User;
import sit.tu_varna.bg.data.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PasswordChangeService implements PasswordChangeOperation {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public PasswordChangeResponse process(PasswordChangeRequest request) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (BCrypt.checkpw(request.getNewPassword(), user.getPassword())) {
            throw new ResourceAlreadyExistsException("Old password matches the new one");
        }

        String newPass = passwordEncoder.encode(request.getNewPassword());

        User changedUser = user.toBuilder()
                .password(newPass)
                .build();

        userRepository.save(changedUser);

        return PasswordChangeResponse.builder()
                .newPassword(user.getPassword())
                .username(user.getUsername())
                .build();
    }
}
