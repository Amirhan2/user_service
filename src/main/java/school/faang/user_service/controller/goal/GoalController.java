package school.faang.user_service.controller.goal;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.goal.GoalDto;
import school.faang.user_service.dto.goal.GoalFilterDto;
import school.faang.user_service.service.goal.GoalService;
import school.faang.user_service.validator.GoalValidator;

import java.util.List;

/**
 * @author Ilia Chuvatkin
 */

@RestController
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;
    private final GoalValidator goalValidator;

    public void createGoal(Long userId, GoalDto goal) {
        goalValidator.validateTitle(goal);
        goalValidator.validateUserId(userId);
        goalService.createGoal(userId, goal);
    }

    public GoalDto updateGoal(Long goalId, GoalDto goal) {
        goalValidator.validateTitleAndGoalId(goalId, goal);
        return goalService.updateGoal(goalId, goal);
    }

    public void deleteGoal(Long goalId) {
        goalService.deleteGoal(goalId);
    }

    public List<GoalDto> getGoalsByUser(Long userId, GoalFilterDto filter) {
        goalValidator.validateUserIdAndFilter(userId, filter);
        return goalService.findGoalsByUser(userId, filter);
    }

    public List<GoalDto> findSubtasksByGoalId(long goalId, GoalFilterDto filter) {
        goalValidator.validateGoalId(goalId);
        return goalService.findSubtasksByGoalId(goalId, filter);
    }
}
