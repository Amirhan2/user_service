package school.faang.user_service.filter;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.UserFilterDto;
import school.faang.user_service.entity.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContactFilterStrategy implements UserFilterStrategy {

    @Override
    public boolean applyFilter(UserFilterDto filter) {
        return filter.getContactPattern() != null && !filter.getContactPattern().isEmpty();
    }

    @Override
    public List<User> filter(List<User> users, UserFilterDto filter) {
        return users.stream()
                .filter(user -> user.getContacts().stream()
                        .anyMatch(contact -> contact.getContact().contains(filter.getContactPattern())))
                .collect(Collectors.toList());
    }
}