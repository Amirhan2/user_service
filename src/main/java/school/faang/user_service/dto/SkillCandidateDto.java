package school.faang.user_service.dto;

import lombok.Data;

@Data
public class SkillCandidateDto {
    private SkillDto skill;
    private long offerAmount;
}