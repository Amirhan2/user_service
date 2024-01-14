package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.service.SkillService;

@RestController
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @PostMapping(value = "/skill")
    public SkillDto create (SkillDto skill) throws DataValidationException {
        validateSkill(skill);

        return skillService.create(skill);
    }

    private void validateSkill (SkillDto skill) {
        if (skill.getTitle() == null || skill.getTitle().isBlank()) {
            throw new DataValidationException("Invalid skill name.");
        }
    }
}
