package school.faang.user_service.service.subscription;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.dto.user.UserFilterDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.filter.user.UserFilter;
import school.faang.user_service.mapper.user.UserMapper;
import school.faang.user_service.repository.SubscriptionRepository;
import school.faang.user_service.validator.subscription.SubscriptionValidator;
import school.faang.user_service.validator.user.UserValidator;

import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserMapper userMapper;
    private final List<UserFilter> userFilters;
    private final UserValidator userValidator;
    private final SubscriptionValidator subscriptionValidator;

    @Transactional
    public void followUser(long followerId, long followeeId) {
        validateTwoUsers(followerId, followeeId);
        userValidator.checkIfFirstUserIdAndSecondUserIdNotEqualsOrElseThrowException(followerId,
                followeeId, "User can't subscribe to himself");
        subscriptionValidator.checkIfSubscriptionExistsAndIfEqualsShouldExistThenThrowException(followerId,
                followeeId, true, "Already subscribed");

        subscriptionRepository.followUser(followerId, followeeId);
    }

    @Transactional
    public void unfollowUser(long followerId, long followeeId) {
        validateTwoUsers(followerId, followeeId);
        userValidator.checkIfFirstUserIdAndSecondUserIdNotEqualsOrElseThrowException(followerId,
                followeeId, "User can't unsubscribe to himself");
        subscriptionValidator.checkIfSubscriptionExistsAndIfEqualsShouldExistThenThrowException(followerId,
                followeeId, false, "Already unsubscribed");

        subscriptionRepository.unfollowUser(followerId, followeeId);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getFollowers(long followerId, UserFilterDto filter) {
        validateUser(followerId);

        Stream<User> userStream = subscriptionRepository.findByFollowerId(followerId);
        List<User> userStreamWithFilter = filterUsers(userStream, filter);

        return userMapper.toDtos(userStreamWithFilter);
    }

    public int getFollowersCount(long followeeId) {
        validateUser(followeeId);
        return subscriptionRepository.findFollowersAmountByFolloweeId(followeeId);
    }

    public int getFollowingCount(long followerId) {
        validateUser(followerId);
        return subscriptionRepository.findFolloweesAmountByFollowerId(followerId);
    }

    @Transactional(readOnly = true)
    public List<UserDto> getFollowing(long followeeId, UserFilterDto filter) {
        validateUser(followeeId);

        Stream<User> userStream = subscriptionRepository.findByFolloweeId(followeeId);
        List<User> userStreamWithFilter = filterUsers(userStream, filter);

        return userMapper.toDtos(userStreamWithFilter);
    }

    private List<User> filterUsers(Stream<User> userStream, UserFilterDto filterDto) {
        if (filterDto != null) {
            return userFilters.stream()
                    .filter(filter -> filter.isApplicable(filterDto))
                    .reduce(userStream,
                            (stream, filter) -> filter.apply(stream, filterDto),
                            (s1, s2) -> s1)
                    .toList();
        }
        return userStream.toList();
    }

    private void validateUser(long userId) {
        userValidator.userIdIsPositiveAndNotNullOrElseThrowValidationException(userId);
        userValidator.userIsExistedOrElseThrowValidationException(userId);
    }

    private void validateTwoUsers(long firstUser, long secondUser) {
        validateUser(firstUser);
        validateUser(secondUser);
    }
}
