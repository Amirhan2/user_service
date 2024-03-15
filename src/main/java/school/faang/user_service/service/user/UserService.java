package school.faang.user_service.service.user;

import school.faang.user_service.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User findUserById(Long userId) {
        if (userId < 1) {
            throw new IllegalArgumentException("id is incorrect");
        }
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("id is not found"));
    }
}

