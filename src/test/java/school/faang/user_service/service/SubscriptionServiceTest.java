package school.faang.user_service.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import school.faang.user_service.dto.UserDto;
import school.faang.user_service.dto.UserFilterDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.mapper.UserMapperImpl;
import school.faang.user_service.repository.SubscriptionRepository;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.validation.SubscriptionValidator;

import java.util.List;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {
    @Mock
    private SubscriptionRepository subscriptionRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private SubscriptionValidator subscriptionValidator;
    @Spy
    private UserMapperImpl userMapper;
    @InjectMocks
    private SubscriptionService subscriptionService;

    private final long followerId = 2;
    private final long followeeId = 1;
    private Stream<User> desiredUsersStream;
    private List<UserDto> desiredUsersDto;

    @BeforeEach
    public void setUp() {
        desiredUsersStream = Stream.of(
                User.builder()
                        .id(0)
                        .build(),
                User.builder()
                        .id(1)
                        .build()
        );
        desiredUsersDto = List.of(
                UserDto.builder()
                        .id(0L)
                        .build(),
                UserDto.builder()
                        .id(1L)
                        .build()
        );
    }

    @Test
    public void shouldAddNewFollowerById() {
        Assertions.assertDoesNotThrow(() -> subscriptionService.followUser(followerId, followeeId));
        Mockito.verify(subscriptionRepository, Mockito.times(1)).followUser(followerId, followeeId);
    }

    @Test
    public void shouldThrowExceptionIfUserSubscribed() {
        Mockito.doThrow(DataValidationException.class).when(subscriptionService)
                        .followUser(followerId, followeeId);

        Assertions.assertThrows(DataValidationException.class, () -> subscriptionService.followUser(followerId, followeeId));
        Mockito.verify(subscriptionRepository, Mockito.times(0)).followUser(followerId, followeeId);
    }

    @Test
    public void shouldThrowExceptionIfFollowerDoesNotExist() {
        Mockito.when(userRepository.existsById(followeeId)).thenReturn(true);
        Mockito.when(userRepository.existsById(followerId)).thenReturn(false);

        Assertions.assertThrows(DataValidationException.class, () -> subscriptionService.followUser(followerId, followeeId));
        Mockito.verify(subscriptionRepository, Mockito.times(0)).followUser(followerId, followeeId);
    }

    @Test
    public void shouldThrowExceptionIfFolloweeDoesNotExist() {
        Mockito.when(userRepository.existsById(followeeId)).thenReturn(false);
        Mockito.lenient().when(userRepository.existsById(followerId)).thenReturn(true);

        Assertions.assertThrows(DataValidationException.class, () -> subscriptionService.followUser(followerId, followeeId));
        Mockito.verify(subscriptionRepository, Mockito.times(0)).followUser(followerId, followeeId);
    }

    @Test
    public void shouldDeleteFollowerById() {
        Assertions.assertDoesNotThrow(() -> subscriptionService.unfollowUser(followerId, followeeId));
        Mockito.verify(subscriptionRepository, Mockito.times(1)).unfollowUser(followerId, followeeId);
    }

    @Test
    public void shouldThrowExceptionIfSubscriptionDoesNotExist() {
        Assertions.assertThrows(DataValidationException.class, () -> subscriptionService.unfollowUser(followerId, followeeId));
        Mockito.verify(subscriptionRepository, Mockito.times(0)).unfollowUser(followerId, followeeId);
    }

    @Test
    public void shouldReturnFollowersList() {
        UserFilterDto filter = new UserFilterDto();

        Mockito.when(subscriptionRepository.findByFolloweeId(followeeId))
                .thenReturn(desiredUsersStream);

        List<UserDto> receivedUsers = subscriptionService.getFollowers(followeeId, filter);

        Assertions.assertEquals(desiredUsersDto, receivedUsers);
        Mockito.verify(subscriptionRepository).findByFolloweeId(followeeId);
    }

    @Test
    public void shouldThrowExceptionWhenFolloweeNotExistsWithFilter() {
        UserFilterDto filter = new UserFilterDto();

        Mockito.when(userRepository.existsById(followeeId)).thenReturn(false);
        Assertions.assertThrows(DataValidationException.class,
                () -> subscriptionService.getFollowers(followeeId, filter));
    }

    @Test
    public void shouldReturnFolloweesList() {
        UserFilterDto filter = new UserFilterDto();

        Mockito.when(subscriptionRepository.findByFollowerId(followerId))
                .thenReturn(desiredUsersStream);

        List<UserDto> receivedUsersDto = subscriptionService.getFollowing(followerId, filter);

        Assertions.assertEquals(desiredUsersDto, receivedUsersDto);
        Mockito.verify(subscriptionRepository).findByFollowerId(followerId);
    }

    @Test
    public void shouldThrowExceptionWhenFollowerNotExistsWithFilter() {
        UserFilterDto filter = new UserFilterDto();

        Mockito.when(subscriptionService.getFollowing(followerId, filter))
                .thenThrow(DataValidationException.class);
        Assertions.assertThrows(DataValidationException.class,
                () -> subscriptionService.getFollowing(followerId, filter));
    }

    @Test
    public void shouldReturnFollowersCount() {
        int desiredFollowersCount = 3;

        Mockito.when(userRepository.existsById(followeeId)).thenReturn(true);
        Mockito.when(subscriptionRepository.findFollowersAmountByFolloweeId(followeeId))
                .thenReturn(desiredFollowersCount);
        int followersCount = subscriptionService.getFollowersCount(followeeId);

        Assertions.assertEquals(desiredFollowersCount, followersCount);
        Mockito.verify(subscriptionRepository).findFollowersAmountByFolloweeId(followeeId);
    }

    @Test
    public void shouldThrowExceptionWhenFolloweeUserNotExists() {
        Mockito.when(userRepository.existsById(followeeId)).thenReturn(false);
        Assertions.assertThrows(DataValidationException.class, () -> subscriptionService.getFollowersCount(followeeId));
        Mockito.verify(subscriptionRepository, Mockito.times(0))
                .findFollowersAmountByFolloweeId(followeeId);
    }

    @Test
    public void shouldReturnFolloweesCount() {
        int desiredFolloweesCount = 3;

        Mockito.when(userRepository.existsById(followerId)).thenReturn(true);
        Mockito.when(subscriptionRepository.findFolloweesAmountByFollowerId(followerId))
                .thenReturn(desiredFolloweesCount);
        int followeesCount = subscriptionService.getFollowingCount(followerId);

        Assertions.assertEquals(desiredFolloweesCount, followeesCount);
        Mockito.verify(subscriptionRepository).findFolloweesAmountByFollowerId(followerId);
    }

    @Test
    public void shouldThrowExceptionWhenFollowerUserNotExists() {
        Mockito.lenient().when(userRepository.existsById(followeeId)).thenReturn(false);
        Assertions.assertThrows(DataValidationException.class, () -> subscriptionService.getFollowingCount(followerId));
        Mockito.verify(subscriptionRepository, Mockito.times(0))
                .findFolloweesAmountByFollowerId(followerId);
    }
}
