package school.faang.user_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.entity.Country;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.person.Person;
import school.faang.user_service.mapper.PersonMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.service.user.ConvertToUserService;
import school.faang.user_service.service.user.CountryService;
import school.faang.user_service.service.user.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConvertToUserServiceTest {

    @Mock
    PersonMapper personMapperMock;
    @Mock
    UserRepository userRepository;
    @Mock
    CountryService countryService;
    @Mock
    UserService userService;
    @InjectMocks
    ConvertToUserService convertToUserService;

    private final Person testPerson = new Person();
    private final User testUser = new User();
    private final Country testCountry = new Country();

    private User prepareTestingUser() {
        Country userCountry = prepareTestingCountry();
        testUser.setUsername("KateBraun");
        testUser.setEmail("kate@example.com");
        testUser.setPhone("1112223333");
        testUser.setCountry(userCountry);
        return testUser;
    }

    private Country prepareTestingCountry() {
        testCountry.setTitle("Canada");
        return testCountry;
    }

    @Test
    public void testPrepareAndSaveUsersIfUsernameAlreadyExist() {
        User savedUser = prepareTestingUser();
        User userFromDB = User.builder()
                .username(savedUser.getUsername())
                .build();
        when(personMapperMock.toUser(testPerson)).thenReturn(savedUser);
        when(userRepository.findByUsernameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(List.of(userFromDB));

        convertToUserService.prepareAndSaveUsers(List.of(testPerson));

        verify(userService, times(0))
                .saveUsers(List.of(savedUser));
    }

    @Test
    public void testPrepareAndSaveUsersIfEmailAlreadyExist() {
        User savedUser = prepareTestingUser();
        User userFromDB = User.builder()
                .email(savedUser.getEmail())
                .build();
        when(personMapperMock.toUser(testPerson)).thenReturn(savedUser);
        when(userRepository.findByUsernameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(List.of(userFromDB));

        convertToUserService.prepareAndSaveUsers(List.of(testPerson));

        verify(userService, times(0))
                .saveUsers(List.of(savedUser));
    }

    @Test
    public void testPrepareAndSaveUsersIfPhoneAlreadyExist() {
        User savedUser = prepareTestingUser();
        User userFromDB = User.builder()
                .phone(savedUser.getPhone())
                .build();
        when(personMapperMock.toUser(testPerson)).thenReturn(savedUser);
        when(userRepository.findByUsernameOrEmailOrPhone(anyString(), anyString(), anyString()))
                .thenReturn(List.of(userFromDB));

        convertToUserService.prepareAndSaveUsers(List.of(testPerson));

        verify(userService, times(0))
                .saveUsers(List.of(savedUser));
    }

    @Test
    public void testPrepareAndSaveUsersIfCountryAlreadyExists() {
        User user = prepareTestingUser();
        Country country = prepareTestingCountry();
        when(personMapperMock.toUser(any())).thenReturn(user);
//        when(userRepository.findByUsernameOrEmailOrPhone(anyString(), anyString(), anyString()))
//                .thenReturn(Collections.emptyList());
//        when(countryService.existsCountryByTitle(any())).thenReturn(true);
//        when(countryService.findAllCountries()).thenReturn(List.of(country));

        convertToUserService.prepareAndSaveUsers(List.of(testPerson));

        verify(countryService, times(0))
                .createCountries(List.of(country));
    }

    @Test
    public void testPrepareAndSaveUsersIfCountryDoesNotExists() {
        User user = prepareTestingUser();
        Country country = prepareTestingCountry();
        when(personMapperMock.toUser(any())).thenReturn(user);
        when(countryService.existsCountryByTitle(any())).thenReturn(false);
        when(countryService.findAllCountries()).thenReturn(List.of(country));

        convertToUserService.prepareAndSaveUsers(List.of(testPerson));

        verify(countryService, times(1))
                .createCountries(List.of(country));
    }

    @Test
    public void testPrepareAndSaveUsersSuccessful() {
        User user = prepareTestingUser();
        Country country = prepareTestingCountry();
        when(personMapperMock.toUser(testPerson)).thenReturn(user);
        when(countryService.existsCountryByTitle(any())).thenReturn(true);
        when(countryService.findAllCountries()).thenReturn(List.of(country));

        convertToUserService.prepareAndSaveUsers(List.of(testPerson));

        verify(userService, times(1))
                .saveUsers(List.of(user));
    }
}