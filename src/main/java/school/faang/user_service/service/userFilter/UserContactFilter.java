package school.faang.user_service.service.userFilter;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.UserFilterDto;
import school.faang.user_service.entity.User;

import java.util.stream.Stream;

@Component
public class UserContactFilter implements UserFilter {
    @Override
    public boolean isApplicable(UserFilterDto userFilterDto) {
        return userFilterDto.getContactPattern() != null;
    }

    @Override
    public void apply(Stream<User> users, UserFilterDto userFilter) {
        users.filter(user -> user.getContacts().stream()
                .anyMatch(contact -> contact.getContact().equals(userFilter.getContactPattern())));
    }
}
