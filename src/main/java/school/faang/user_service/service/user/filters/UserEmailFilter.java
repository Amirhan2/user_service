package school.faang.user_service.service.user.filters;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.user.UserFilterDto;
import school.faang.user_service.entity.User;

import java.util.stream.Stream;

@Component
public class UserEmailFilter implements UserFilter<UserFilterDto, User> {
    @Override
    public boolean isApplicable(UserFilterDto userFilterDto) {
        return userFilterDto.getEmailPattern() != null;
    }

    @Override
    public Stream<User> apply(Stream<User> users, UserFilterDto filters) {
        return users.filter(user -> user.getEmail().contains(filters.getEmailPattern()));
    }
}