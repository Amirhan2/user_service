package school.faang.user_service.filter.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import school.faang.user_service.dto.UserFilterDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.filters.user.UserCityFilter;
import school.faang.user_service.util.TestDataFactory;

import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class UserCityFilterTest {

    private UserCityFilter cityFilter = new UserCityFilter();
    private User user;

    @BeforeEach
    public void setUp() {
        user = TestDataFactory.createUser();
    }

    @Test
    public void givenValidUserWhenApplyThenReturnUser() {
        // Given
        var filter = TestDataFactory.createFilterDto();
        filter.setCityPattern("Moscow");

        // When
        var result = cityFilter.apply(Stream.of(user), filter);

        // Then
        assertNotNull(result);
        assertEquals(result.findFirst().get(), user);
    }

    @Test
    public void givenValidUserWhenApplyThenReturnEmptyStream() {
        // Given
        var user = TestDataFactory.createUser();
        UserFilterDto filter = new UserFilterDto();
        filter.setCityPattern("Spb");

        // When
        var result = cityFilter.apply(Stream.of(user), filter);

        // Then
        assertNotNull(result);
        assertFalse(result.findAny().isPresent());
    }
}
