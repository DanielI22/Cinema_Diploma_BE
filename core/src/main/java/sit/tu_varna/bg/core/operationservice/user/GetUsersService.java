package sit.tu_varna.bg.core.operationservice.user;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import sit.tu_varna.bg.api.dto.UserDto;
import sit.tu_varna.bg.api.operation.user.getall.GetUsersOperation;
import sit.tu_varna.bg.api.operation.user.getall.GetUsersRequest;
import sit.tu_varna.bg.api.operation.user.getall.GetUsersResponse;
import sit.tu_varna.bg.core.mapper.UserMapper;
import sit.tu_varna.bg.entity.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class GetUsersService implements GetUsersOperation {
    @Inject
    UserMapper userMapper;

    @Override
    public GetUsersResponse process(GetUsersRequest request) {
        List<UserDto> users = User.listAll()
                .stream()
                .filter(User.class::isInstance)
                .map(User.class::cast)
                .filter(u -> !u.isDeleted())
                .sorted(Comparator.comparing(User::getCreatedOn).reversed())
                .map(u -> userMapper.userToUserDto(u))
                .collect(Collectors.toList());

        return GetUsersResponse.builder()
                .users(users)
                .build();
    }
}
