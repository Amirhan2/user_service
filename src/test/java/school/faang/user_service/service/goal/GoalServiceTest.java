package school.faang.user_service.service.goal;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.GoalDto;

import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.entity.goal.GoalStatus;
import school.faang.user_service.mapper.GoalMapper;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.goal.GoalRepository;
import school.faang.user_service.service.SkillService;
import school.faang.user_service.service.user.UserService;
import school.faang.user_service.validation.goal.GoalValidation;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoalServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private GoalValidation goalValidation;
    @Mock
    private SkillService skillService;
    @Mock
    private GoalRepository goalRepository;
    @Mock
    private SkillRepository skillRepository;
    @Mock
    private GoalMapper goalMapper;
    @InjectMocks
    private GoalService goalService;


    @Test
    void testCreateGoal() {
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setTitle("Goal");
        goal.setDescription("Something");
        Skill skill_1 = new Skill();
        skill_1.setId(1L);
        Skill skill_2 = new Skill();
        skill_2.setId(2L);
        goal.setSkillsToAchieve(List.of(skill_1, skill_2));
        GoalDto goalDto = new GoalDto();
        goalDto.setSkillId(List.of(1L, 2L));
        Long userId = 1L;
        User user = new User();
        user.setId(1L);

        when(goalMapper.toEntity(goalDto)).thenReturn(goal);
        when(userService.findById(userId)).thenReturn(user);
        when(skillService.findById(1L)).thenReturn(skill_1);
        when(skillService.findById(2L)).thenReturn(skill_2);

        goalService.createGoal(userId, goalDto);

        verify(goalRepository, times(1)).save(goal);
    }

    @Test
    void testUpdateGoalWithStatusCompleted() {
        Goal goalOld = new Goal();
        Long goalOldId = 1L;
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setStatus(GoalStatus.COMPLETED);
        GoalDto goalDto = new GoalDto();
        goalDto.setId(1L);
        goalDto.setSkillId(List.of(1L, 2L));
        Skill skill_1 = new Skill();
        skill_1.setId(1L);
        Skill skill_2 = new Skill();
        skill_2.setId(2L);
        goal.setSkillsToAchieve(List.of(skill_1, skill_2));
        User user1 = new User();
        user1.setSkills(List.of(skill_1));
        User user2 = new User();
        user2.setSkills(List.of(skill_1));
        when(goalMapper.toEntity(goalDto)).thenReturn(goal);
        when(goalRepository.findById(goalOldId)).thenReturn(Optional.of(goalOld));
        when(goalRepository.findUsersByGoalId(goal.getId())).thenReturn(List.of(user1, user2));

        when(skillService.findById(1L)).thenReturn(skill_1);
        when(skillService.findById(2L)).thenReturn(skill_2);

        goalService.updateGoal(goalOldId, goalDto);

        verify(goalRepository, times(1)).save(goal);
        verify(skillRepository, times(2)).assignSkillToUser(anyLong(), anyLong());
    }

    @Test
    void testDeleteGoal() {
        goalService.deleteGoal(1L);
        verify(goalRepository, times(1)).deleteById(1L);
    }

    @Test
    void testUpdateGoalWithStatusActive() {
        Goal goalOld = new Goal();
        Long goalOldId = 1L;
        Skill skill_1 = new Skill();
        skill_1.setId(1L);
        Skill skill_2 = new Skill();
        skill_2.setId(2L);
        GoalDto goalDto = new GoalDto();
        goalDto.setId(1L);
        goalDto.setSkillId(List.of(1L, 2L));
        Goal goal = new Goal();
        goal.setId(1L);
        goal.setStatus(GoalStatus.ACTIVE);

        when(goalMapper.toEntity(goalDto)).thenReturn(goal);
        when(skillService.findById(1L)).thenReturn(skill_1);
        when(skillService.findById(2L)).thenReturn(skill_2);
        when(goalRepository.findById(goalOldId)).thenReturn(Optional.of(goalOld));

        goalService.updateGoal(goalOldId, goalDto);

        verify(goalRepository, times(1)).save(goal);
    }
}

