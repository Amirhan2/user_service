package school.faang.user_service.service.impl.goal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.goal.GoalDto;
import school.faang.user_service.dto.goal.GoalFilterDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.entity.goal.GoalStatus;
import school.faang.user_service.filter.goal.GoalFilter;
import school.faang.user_service.mapper.goal.GoalMapper;
import school.faang.user_service.repository.goal.GoalRepository;
import school.faang.user_service.service.GoalService;
import school.faang.user_service.service.SkillService;
import school.faang.user_service.validator.goal.GoalValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {
    private final GoalRepository goalRepository;
    private final static int MAX_NUMBER_ACTIVE_GOALS = 3;
    private final SkillService skillService;
    private final GoalValidator goalValidator;
    private final List<GoalFilter> filters;
    private final GoalMapper goalMapper;

    @Override
    public GoalDto createGoal(long userId, GoalDto goalDto) {
        goalValidator.validateCreationGoal(userId, MAX_NUMBER_ACTIVE_GOALS);

        Goal saveGoal = goalRepository.create(goalDto.tittle(),
                goalDto.description(),
                goalDto.parentId());

        List<Skill> skills = skillService.getSkillsByTitle(goalDto.titleSkills());
        saveGoal.setSkillsToAchieve(skills);
        goalRepository.save(saveGoal);
        return goalMapper.toDto(saveGoal);
    }

    @Override
    public GoalDto updateGoal(long goalId, GoalDto goalDto) {
        Goal goal = goalValidator.validateUpdate(goalId, goalDto);

        List<Skill> skills = skillService.getSkillsByTitle(goalDto.titleSkills());
        assignNewSkillToGoal(goal, skills);
        goalRepository.save(goalMapper.toEntity(goalDto));

        if (goal.getStatus() == GoalStatus.COMPLETED) {
            assignGoalSkillsToUsers(goalId, skills);
        }
        return goalMapper.toDto(goal);
    }

    private void assignGoalSkillsToUsers(long goalId, List<Skill> skills) {
        List<User> users = goalRepository.findUsersByGoalId(goalId);
        users.forEach(user -> skills
                .forEach(skill -> skillService.assignSkillToUser(skill.getId(), user.getId())));
    }

    private void assignNewSkillToGoal(Goal goal, List<Skill> newSkills) {
        skillService.deleteSkillFromGoal(goal.getId());
        goal.setSkillsToAchieve(newSkills);
    }

    @Override
    public void deleteGoal(long goalId) {
        goalRepository.deleteById(goalId);
    }

    @Override
    public List<GoalDto> findSubtasksByGoalId(long goalId, GoalFilterDto filterDto) {
        List<Goal> goals = goalRepository.findByParent(goalId).toList();
        return goalMapper.toDto(getGoalAfterFilters(goals, filterDto));
    }

    @Override
    public List<GoalDto> getGoalsByUser(long userId, GoalFilterDto filterDto) {
        List<Goal> goals = goalRepository.findGoalsByUserId(userId).toList();
        return goalMapper.toDto(getGoalAfterFilters(goals, filterDto));
    }

    private List<Goal> getGoalAfterFilters(List<Goal> goals, GoalFilterDto filterDto) {
        return filters.stream()
                .filter(filter -> filter.isApplicable(filterDto))
                .reduce(goals.stream(), (stream, filter) ->
                        filter.apply(stream, filterDto), (s1, s2) -> s1).toList();
    }

    @Override
    public void removeGoals(List<Goal> goals){
        goalRepository.deleteAll(goals);
    }
}
