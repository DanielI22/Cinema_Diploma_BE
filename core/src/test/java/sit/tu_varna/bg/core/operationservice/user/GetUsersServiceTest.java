package sit.tu_varna.bg.core.operationservice.user;

import io.quarkus.panache.mock.PanacheMock;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import sit.tu_varna.bg.api.dto.UserDto;
import sit.tu_varna.bg.api.operation.user.getall.GetUsersRequest;
import sit.tu_varna.bg.api.operation.user.getall.GetUsersResponse;
import sit.tu_varna.bg.core.mapper.UserMapper;
import sit.tu_varna.bg.entity.User;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@QuarkusTest
class GetUsersServiceTest {

    @InjectMocks
    GetUsersService getUsersService;

    @Mock
    UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        PanacheMock.mock(User.class);
    }

    @Test
    void process_ReturnsUsersSuccessfully() {
        // Arrange
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@example.com");
        user1.setCreatedOn(Instant.now());

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@example.com");
        user2.setCreatedOn(Instant.now().minusSeconds(3600));

        UserDto userDto1 = new UserDto();
        userDto1.setId(user1.getId().toString());
        userDto1.setEmail(user1.getEmail());

        UserDto userDto2 = new UserDto();
        userDto2.setId(user2.getId().toString());
        userDto2.setEmail(user2.getEmail());

        when(User.listAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);
        when(userMapper.userToUserDto(user2)).thenReturn(userDto2);

        GetUsersRequest request = new GetUsersRequest();

        // Act
        GetUsersResponse response = getUsersService.process(request);

        // Assert
        verify(userMapper, times(1)).userToUserDto(user1);
        verify(userMapper, times(1)).userToUserDto(user2);

        List<UserDto> expectedUsers = Arrays.asList(userDto1, userDto2);
        assertEquals(expectedUsers, response.getUsers());
    }

    @Test
    void process_FiltersDeletedUsers() {
        // Arrange
        User user1 = new User();
        user1.setId(UUID.randomUUID());
        user1.setEmail("user1@example.com");
        user1.setCreatedOn(Instant.now());

        User user2 = new User();
        user2.setId(UUID.randomUUID());
        user2.setEmail("user2@example.com");
        user2.setCreatedOn(Instant.now().minusSeconds(3600));
        user2.setDeleted(true);  // This user should be filtered out

        UserDto userDto1 = new UserDto();
        userDto1.setId(user1.getId().toString());
        userDto1.setEmail(user1.getEmail());

        when(User.listAll()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.userToUserDto(user1)).thenReturn(userDto1);

        GetUsersRequest request = new GetUsersRequest();

        // Act
        GetUsersResponse response = getUsersService.process(request);

        // Assert
        verify(userMapper, times(1)).userToUserDto(user1);
        verify(userMapper, never()).userToUserDto(user2);

        List<UserDto> expectedUsers = List.of(userDto1);
        assertEquals(expectedUsers, response.getUsers());
    }
}
