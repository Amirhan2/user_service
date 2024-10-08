package school.faang.user_service.controller.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.user.UserDto;
import school.faang.user_service.dto.user.UserFilterDto;
import school.faang.user_service.service.user.UserDeactivationService;
import school.faang.user_service.service.user.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    private final UserDeactivationService userDeactivationService;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable long userId) {
        return userService.getUser(userId);
    }

    @PostMapping
    public List<UserDto> getUsersByIds(@RequestBody List<Long> ids) {
        return userService.getUsersByIds(ids);
    }

    @PatchMapping("/{userId}/deactivate")
    @ResponseStatus(HttpStatus.OK)
    public void deactivateUserAccount(@PathVariable Long userId) {
        userDeactivationService.deactivateAccount(userId);
    }

    @PostMapping("/registration")
    public UserDto createUserAccount(@RequestBody @Valid UserRegistrationDto userRegistrationDto) {
        return userService.registerUser(userRegistrationDto);
    }

    @PutMapping("/{userId}/avatar")
    public void uploadUserAvatar(@PathVariable Long userId, @RequestBody MultipartFile file) {
        userProfilePicService.uploadUserAvatar(userId, file);
    }

    @GetMapping(value = "/{userId}/avatar", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> downloadUserAvatar(@PathVariable Long userId) {
        try {
            byte[] img = userProfilePicService.downloadUserAvatar(userId).readAllBytes();
            return new ResponseEntity<>(img, HttpStatus.OK);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to download picture", ex.getCause());
        }
    }

    @DeleteMapping("/{userId}/avatar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserAvatar(@PathVariable Long userId) {
        userProfilePicService.deleteUserAvatar(userId);
    }

    @PostMapping("/upload")
    public List<UserDto> parseUserCsvFile(@RequestBody MultipartFile multipartFile) {
        return userService.saveUsersFromCsvFile(multipartFile);
    }

    @GetMapping
    public List<UserDto> getFilteredUsers(@RequestBody @Valid UserFilterDto filter) {
        return userService.users(filter);
    }
}
