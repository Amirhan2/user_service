package school.faang.user_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.dto.UserDTO;
import school.faang.user_service.dto.UserFilterDTO;
import school.faang.user_service.entity.User;
import school.faang.user_service.filter.ExperienceFilter;
import school.faang.user_service.filter.NameFilter;
import school.faang.user_service.repository.SubscriptionRepository;
import school.faang.user_service.repository.filter.UserFilter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Transactional
    public void followUser(Long followerId, Long followeeId) {
        log.info("Пользователь {} пытается подписаться на пользователя {}", followerId, followeeId);
        if (followerId.equals(followeeId)) {
            log.warn("Пользователь {} не может подписаться на себя.", followerId);
            throw new IllegalArgumentException("Пользователь не может подписаться на себя.");
        }
        if (subscriptionRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            log.warn("Подписка между пользователями {} и {} уже существует.", followerId, followeeId);
            throw new IllegalArgumentException("Подписка уже существует.");
        }
        subscriptionRepository.followUser(followerId, followeeId);
        log.info("Пользователь {} успешно подписался на пользователя {}.", followerId, followeeId);
    }

    @Transactional
    public void unfollowUser(Long followerId, Long followeeId) {
        log.info("Пользователь {} пытается отписаться от пользователя {}", followerId, followeeId);
        try {
            if (!subscriptionRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
                log.warn("Подписка между пользователями {} и {} не существует.", followerId, followeeId);
                throw new IllegalArgumentException("Подписка не существует.");
            }
            subscriptionRepository.unfollowUser(followerId, followeeId);
            log.info("Пользователь {} успешно отписался от пользователя {}.", followerId, followeeId);
        } catch (IllegalArgumentException ex) {
            log.warn("Ошибка при попытке отписаться: {}", ex.getMessage());
            throw ex; // Пробрасываем исключение дальше, чтобы обработать его в GlobalExceptionHandler
        } catch (Exception ex) {
            log.error("Произошла ошибка при отписке пользователя: followerId={}, followeeId={}", followerId, followeeId, ex);
            throw new RuntimeException("Не удалось отписаться от пользователя.", ex);
        }
    }

    public List<UserDTO> getFollowers(Long userId, UserFilterDTO filter) {
        log.info("Запрос на получение подписчиков пользователя {}", userId);
        if (filter == null) {
            filter = new UserFilterDTO();
        }
        List<UserDTO> followers = filterUsers(subscriptionRepository.findByFolloweeId(userId).collect(Collectors.toList()), filter);
        log.info("Найдено {} подписчиков для пользователя {}", followers.size(), userId);
        return followers;
    }

    public boolean subscriptionExists(Long followerId, Long followeeId) {
        log.info("Проверка существования подписки между пользователями {} и {}", followerId, followeeId);
        boolean exists = subscriptionRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId);
        log.info("Подписка между пользователями {} и {} {}", followerId, followeeId, exists ? "существует." : "не существует.");
        return exists;
    }

    public List<UserDTO> filterUsers(List<User> users, UserFilterDTO filter) {
        log.info("Применение фильтров к пользователям. Количество пользователей: {}", users.size());
        List<UserFilter> filters = new ArrayList<>();
        if (filter.getNamePattern() != null) {
            filters.add(new NameFilter(filter.getNamePattern()));
            log.info("Фильтр по имени с паттерном: {}", filter.getNamePattern());
        }
        if (filter.getExperienceMin() != null || filter.getExperienceMax() != null) {
            filters.add(new ExperienceFilter(filter.getExperienceMin(), filter.getExperienceMax()));
            log.info("Фильтр по опыту: min={}, max={}", filter.getExperienceMin(), filter.getExperienceMax());
        }

        List<UserDTO> filteredUsers = users.stream()
            .filter(user -> filters.stream().allMatch(f -> f.filter(user)))
            .map(user -> new UserDTO(user.getId(), user.getUsername(), user.getEmail()))
            .collect(Collectors.toList());

        log.info("Найдено {} пользователей после применения фильтров.", filteredUsers.size());
        return filteredUsers;
    }
}
