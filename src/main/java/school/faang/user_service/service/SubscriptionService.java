package school.faang.user_service.service;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.dto.user.UserFilterDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.event.FollowerEvent;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.filter.user.UserFilter;
import school.faang.user_service.mapper.user.UserMapper;
import school.faang.user_service.publisher.FollowerMessagePublisher;
import school.faang.user_service.repository.SubscriptionRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserMapper userMapper;
    private final List<UserFilter> userFilters;
    private final FollowerMessagePublisher followerMessagePublisher;

    @Transactional
    public void follow(long followerId, long followeeId) {
        if (!subscriptionRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            subscriptionRepository.followUser(followerId, followeeId);
            sendFollowerEvent(followerId, followeeId);
        } else {
            log.info("Method follow get exception");
            throw new DataValidationException("You are already subscribed to this user");
        }
    }

    @Transactional
    public void unfollow(long followerId, long followeeId) {
        if (subscriptionRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            subscriptionRepository.unfollowUser(followerId, followeeId);
        } else {
            log.info("Method unfollow get exception");
            throw new DataValidationException("Subscription not found");
        }
    }

    @Transactional(readOnly = true)
    public List<UserDto> getFollowers(long followerId, UserFilterDto filters) {
        Stream<User> followers = subscriptionRepository.findByFollowerId(followerId);
        return filterUsers(filters, followers);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getFollowing(long followeeId, UserFilterDto filters) {
        Stream<User> followee = subscriptionRepository.findByFolloweeId(followeeId);
        return filterUsers(filters, followee);
    }

    @Transactional(readOnly = true)
    public long getFollowersCount(long followeeId) {
        return subscriptionRepository.findFollowersAmountByFolloweeId(followeeId);
    }

    @Transactional(readOnly = true)
    public long getFollowingCount(long followerId) {
        return subscriptionRepository.findFollowersAmountByFollowerId(followerId);
    }

    @NotNull
    private List<UserDto> filterUsers(UserFilterDto filters, Stream<User> followees) {
        List<UserFilter> applicableFilters = userFilters.stream()
                .filter(userFilter -> userFilter.isApplicable(filters))
                .toList();
        List<User> filteredUsers = followees.toList();
        for (UserFilter userFilter : applicableFilters) {
            filteredUsers = userFilter.apply(filteredUsers.stream(), filters).toList();
        }
        return filteredUsers.stream()
                .map(userMapper::toDto).toList();
    }

    private void sendFollowerEvent(long followerId, long followeeId) {
        FollowerEvent followerEvent = FollowerEvent.builder()
                .followerId(followerId)
                .followeeId(followeeId)
                .eventTime(LocalDateTime.now()).build();
        followerMessagePublisher.publish(followerEvent);
    }
}
