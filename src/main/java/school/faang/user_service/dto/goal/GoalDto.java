package school.faang.user_service.dto.goal;

import lombok.Data;
import school.faang.user_service.entity.goal.GoalStatus;

import java.util.List;

@Data
public class GoalDto {
    private Long id;
    private String title;
    private String description;
    private Long parent;
    private GoalStatus status;
    private List<Long> skillIds;
}