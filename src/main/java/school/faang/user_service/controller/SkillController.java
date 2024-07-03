package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.service.SkillService;

@Component
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    public SkillDto create(SkillDto skill) {
        validateSkill(skill);

        return skillService.create(skill);
    }

    private void validateSkill(SkillDto skill) {
        if (skill.getTitle() == null) {
            throw new DataValidationException("Skill title is required");
        }

        if (skill.getTitle().isBlank()) {
            throw new DataValidationException("Skill with id " + skill.getId() + " is blank");
        }
    }
}
