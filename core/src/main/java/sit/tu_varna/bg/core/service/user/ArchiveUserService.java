package sit.tu_varna.bg.core.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.user.archive.ArchiveUserOperation;
import sit.tu_varna.bg.api.operation.user.archive.ArchiveUserRequest;
import sit.tu_varna.bg.api.operation.user.archive.ArchiveUserResponse;
import sit.tu_varna.bg.data.entity.User;
import sit.tu_varna.bg.data.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class ArchiveUserService implements ArchiveUserOperation {
    private final UserRepository userRepository;

    @Override
    public ArchiveUserResponse process(ArchiveUserRequest request) {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userRepository.delete(user);

        return ArchiveUserResponse.builder()
                .username(user.getUsername())
                .deleted(true)
                .build();
    }
}
