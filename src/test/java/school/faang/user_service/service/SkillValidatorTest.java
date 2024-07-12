package school.faang.user_service.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.validator.SkillValidator;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class SkillValidatorTest {

    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillValidator skillValidator;

    @Test
    public void testSkillNameIsNull() {
        Exception exception = Assert.assertThrows(NullPointerException.class,
                () -> skillValidator.validateSkill(new SkillDto(1L, null)));
        String actualMessage = exception.getMessage();
        String expectedMessage = "skill name is null";
        Assert.assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testSkillNameIsEmpty() {
        Assert.assertThrows(DataValidationException.class,
                () -> skillValidator.validateSkill(new SkillDto(1L, "")));
    }

    @Test
    public void testSkillNameIsBlank() {
        Assert.assertThrows(DataValidationException.class,
                () -> skillValidator.validateSkill(new SkillDto(1L, "  ")));
    }

    @Test
    public void testSkillExistsByTitle() {
        Mockito.when(skillRepository.existsByTitle("title")).thenReturn(true);
        Assert.assertThrows(DataValidationException.class,
                () -> skillValidator.validateSkill(new SkillDto(1L, "title")));
    }

    @Test
    public void testValidateUserSkills() {
        List<Skill> emptySKills = new ArrayList<>();
        Assert.assertThrows(DataValidationException.class, () -> skillValidator.validateUserSkills(emptySKills));
    }
}