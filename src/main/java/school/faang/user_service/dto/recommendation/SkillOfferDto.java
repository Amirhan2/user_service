package school.faang.user_service.dto.recommendation;

import lombok.Data;

@Data
public class SkillOfferDto {
    private Long id;
    private long skillId;
    private long recommendationId;
}
