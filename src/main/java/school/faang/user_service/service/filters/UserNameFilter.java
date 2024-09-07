package school.faang.user_service.service.filters;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.UserFilterDto;
import school.faang.user_service.entity.User;

import java.util.stream.Stream;

@Component
public class UserNameFilter implements UserFilter {
    @Override
    public boolean isApplicable(UserFilterDto userFilterDto) {
        return userFilterDto.getNamePattern() != null;
    }

    @Override
    public void apply(Stream<User> userStream, UserFilterDto filterDto) {
        userStream.filter(user -> user.getUsername().contains(filterDto.getNamePattern()));
    }
}
