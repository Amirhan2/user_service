package school.faang.user_service.filter.user;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.user.UserFilterDto;
import school.faang.user_service.entity.User;

import java.util.stream.Stream;

@Component
public class EmailFilter implements UserFilter {

    @Override
    public boolean isApplicable(UserFilterDto userFilterDto) {
        return userFilterDto.getEmailPattern() != null;
    }

    @Override
    public Stream<User> apply(Stream<User> userStream, UserFilterDto userFilterDto) {
        return userStream.filter(filter -> filter.getEmail().equals(userFilterDto.getEmailPattern()));
    }
}