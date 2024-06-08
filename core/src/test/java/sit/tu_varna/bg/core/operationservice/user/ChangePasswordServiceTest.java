package sit.tu_varna.bg.core.operationservice.user;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.exception.ResourceNotFoundException;
import sit.tu_varna.bg.api.operation.user.changepassword.ChangePasswordRequest;
import sit.tu_varna.bg.api.operation.user.changepassword.ChangePasswordResponse;
import sit.tu_varna.bg.core.common.KeycloakService;
import sit.tu_varna.bg.entity.User;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@QuarkusTest
public class ChangePasswordServiceTest {

    @InjectMocks
    ChangePasswordService changePasswordService;

    @Mock
    KeycloakService keycloakService;

    UUID userId;
    User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(User.class);

        userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setEmail("test@example.com");
    }

    @Test
    void process_SuccessfulPasswordChange() {
        // Arrange
        when(User.findByIdOptional(userId)).thenReturn(Optional.of(user));
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .userId(userId)
                .password("newPassword")
                .build();

        // Act
        ChangePasswordResponse response = changePasswordService.process(request);

        // Assert
        verify(keycloakService, times(1)).updatePassword(userId.toString(), "newPassword");
        assertEquals(userId.toString(), response.getUserId());
    }

    @Test
    void process_UserNotFound() {
        // Arrange
        when(User.findByIdOptional(userId)).thenReturn(Optional.empty());
        ChangePasswordRequest request = ChangePasswordRequest.builder()
                .userId(userId)
                .password("newPassword")
                .build();

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> changePasswordService.process(request));

        verify(keycloakService, never()).updatePassword(anyString(), anyString());
    }
}
