package school.faang.user_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.event.Event;
import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.mapper.UserMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.event.EventRepository;
import school.faang.user_service.repository.goal.GoalRepository;
import school.faang.user_service.service.mentorship.MentorshipService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private static final String MESSAGE_USER_NOT_EXIST = "User does not exist";
    private static final String MESSAGE_USER_ALREADY_DEACTIVATED = "User is already deactivated";
    private static final int VALID_ID = 1;
    @Mock
    private MentorshipService mentorshipService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private GoalRepository goalRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private UserMapper mapper;
    @InjectMocks
    private UserService userService;
    private User user;
    private UserDto dtoUser;
    private List<Long> ids;
    private List<UserDto> dtoList;

    @BeforeEach
    void setUp() {
        //Arrange
        user = new User();
        user.setId(VALID_ID);
        user.setActive(true);
        Goal goal = new Goal();
        goal.setId((long) VALID_ID);
        goal.setUsers(List.of(new User()));
        user.setGoals(List.of(goal));
        user.setOwnedEvents(List.of(new Event(), new Event()));

        dtoUser = new UserDto();
        ids = List.of(1L);
        dtoList = new ArrayList<>();
    }

    @Test
    public void testUserIsNotInDb() {
        //Act
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        //Assert
        assertEquals(
                MESSAGE_USER_NOT_EXIST,
                assertThrows(
                        RuntimeException.class,
                        () -> userService.deactivatesUserProfile(user.getId())).getMessage());
    }

    @Test
    public void testUserAlreadyDeactivated() {
        //Arrange
        user.setActive(false);
        //Act
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        //Assert
        assertEquals(
                MESSAGE_USER_ALREADY_DEACTIVATED,
                assertThrows(
                        RuntimeException.class,
                        () -> userService.deactivatesUserProfile(user.getId())).getMessage());
    }

    @Test
    public void testGoalDeletedById() {
        //Act
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        //Assert
        userService.deactivatesUserProfile(user.getId());
        Mockito.verify(goalRepository).deleteById(anyLong());
    }

    @Test
    public void testEventDeletedById() {
        //Act
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        //Assert
        userService.deactivatesUserProfile(user.getId());
        Mockito.verify(eventRepository, Mockito.times(user.getOwnedEvents().size())).deleteById(anyLong());
    }

    @Test
    public void testUserSave() {
        //Act
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.when(mentorshipService.stopMentorship(any())).thenReturn(user);
        //Assert
        userService.deactivatesUserProfile(user.getId());
        Mockito.verify(userRepository).save(user);
    }

    @Test
    public void testUserToUserDto() {
        //Act
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.when(mentorshipService.stopMentorship(any())).thenReturn(user);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        //Assert
        userService.deactivatesUserProfile(user.getId());
        Mockito.verify(mapper).toDto(user);
    }

    @Test
    @DisplayName("Тест получаем пользователя")
    public void testGetUser() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.when(mapper.toDto(any())).thenReturn(dtoUser);
        assertEquals(dtoUser, userService.getUser(1L));
    }

    @Test
    @DisplayName("Тест получение пользователя на исключение")
    public void testGetUser_whenException() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                userService.getUser(1L));
    }

    @Test
    @DisplayName("Тест получаем список всех пользователей")
    public void testGetUsersByIds() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Mockito.when(mapper.toDto(any())).thenReturn(dtoUser);
        dtoUser = mapper.toDto(user);
        dtoList = List.of(dtoUser);
        assertEquals(dtoList, userService.getUsersByIds(ids));
    }

    @Test
    @DisplayName("Тест исключение при получении списка пользователей")
    public void testGetUsersByIds_whenException() {
        Mockito.when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () ->
                userService.getUsersByIds(ids));
    }

}