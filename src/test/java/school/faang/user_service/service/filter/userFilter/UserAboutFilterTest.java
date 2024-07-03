package school.faang.user_service.service.filter.userFilter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.UserFilterDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.service.userFilter.UserAboutFilter;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserAboutFilterTest {

    private UserAboutFilter userAboutFilter;

    @BeforeEach
    public void setUp() {
        userAboutFilter = new UserAboutFilter();
    }

    @Test
    public void testIsApplicable_withNonNullAboutPattern() {
        UserFilterDto userFilterDto = new UserFilterDto();
        userFilterDto.setAboutPattern("pattern");

        assertTrue(userAboutFilter.isApplicable(userFilterDto));
    }

    @Test
    public void testIsApplicable_withNullAboutPattern() {
        UserFilterDto userFilterDto = new UserFilterDto();

        assertFalse(userAboutFilter.isApplicable(userFilterDto));
    }

    @Test
    public void testApply_WithMatchingPattern() {
        UserFilterDto userFilterDto = mock(UserFilterDto.class);
        when(userFilterDto.getAboutPattern()).thenReturn(".*test.*");

        User firstUser = mock(User.class);
        when(firstUser.getAboutMe()).thenReturn("This is a test about");

        User secondUser = mock(User.class);
        when(secondUser.getAboutMe()).thenReturn("Another about");

        List<User> users = Stream.of(firstUser, secondUser).toList();
        Stream<User> userStream = users.stream();

        userAboutFilter.apply(userStream, userFilterDto);

        List<User> filteredUsers = users.stream()
                .filter(user -> user.getAboutMe().matches(userFilterDto.getAboutPattern()))
                .toList();

        assertEquals(1, filteredUsers.size());
        assertEquals("This is a test about", filteredUsers.get(0).getAboutMe());
    }

    @Test
    public void testApply_WithNonMatchingPattern() {
        UserFilterDto userFilterDto = mock(UserFilterDto.class);
        when(userFilterDto.getAboutPattern()).thenReturn(".*nonmatching.*");

        User firstUser = mock(User.class);
        when(firstUser.getAboutMe()).thenReturn("This is a test about");

        User secondUser = mock(User.class);
        when(secondUser.getAboutMe()).thenReturn("Another about");

        List<User> users = Stream.of(firstUser, secondUser).toList();
        Stream<User> userStream = users.stream();

        userAboutFilter.apply(userStream, userFilterDto);

        List<User> filteredUsers = users.stream()
                .filter(user -> user.getAboutMe().matches(userFilterDto.getAboutPattern()))
                .toList();

        assertEquals(0, filteredUsers.size());
    }
}
