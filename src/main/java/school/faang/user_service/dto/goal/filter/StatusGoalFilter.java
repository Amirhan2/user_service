package school.faang.user_service.dto.goal.filter;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.GoalFilterDto;
import school.faang.user_service.entity.goal.Goal;

import java.util.stream.Stream;

@Component
public class StatusGoalFilter implements GoalFilter {

    @Override
    public boolean isApplicable(GoalFilterDto filter) {
        return filter.status() != null;
    }

    @Override
    public Stream<Goal> apply(Stream<Goal> goals, GoalFilterDto filter) {
        return goals.filter(goal -> goal.getStatus() == filter.status());
    }
}