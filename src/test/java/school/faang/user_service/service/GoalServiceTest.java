package school.faang.user_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.goal.GoalDto;
import school.faang.user_service.dto.goal.GoalFilterDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.entity.goal.GoalStatus;
import school.faang.user_service.filter.goal.GoalFilter;
import school.faang.user_service.mapper.goal.GoalMapper;
import school.faang.user_service.repository.goal.GoalRepository;
import school.faang.user_service.service.goal.GoalServiceImpl;
import school.faang.user_service.service.skill.SkillService;
import school.faang.user_service.validator.goal.GoalValidator;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GoalServiceTest {
    @InjectMocks
    private GoalServiceImpl goalServiceImpl;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private SkillService skillService;

    @Mock
    private GoalValidator goalValidator;

    @Mock
    private List<GoalFilter> filters;

    @Spy
    private GoalMapper goalMapper = Mappers.getMapper(GoalMapper.class);

    private long userId;
    private int maxGoal;
    private long goalId;
    private GoalDto goalDto;
    private List<Skill> skills;
    private Goal goal;
    private Goal updateGoal1;
    private User user;

    @BeforeEach
    public void setUp() {
        userId = 1;
        maxGoal = 3;
        goalId = 1;
        goalDto = new GoalDto("goal title",
                "goal description",
                null,
                List.of("Skill1"),
                GoalStatus.ACTIVE);
        skills = List.of(Skill.builder()
                .id(1L)
                .title("Skill2")
                .build(), Skill.builder().title("Skill1").build());
        goal = new Goal();
        updateGoal1 = Goal.builder()
                .id(1L)
                .build();
        user = new User();
    }

    @Test
    public void createGoalIsCalledWithCorrectParameters() {
        Mockito.doNothing().when(goalValidator)
                .validateCreationGoal(userId, maxGoal);

        Goal saveGoal = new Goal();
        Mockito.when(goalRepository.create(goalDto.tittle(),
                goalDto.description(),
                goalDto.parentId())).thenReturn(saveGoal);

        goalServiceImpl.createGoal(userId, goalDto);

        Mockito.verify(goalRepository)
                .create(goalDto.tittle(), goalDto.description(), goalDto.parentId());
    }

    @Test
    public void createGoalGetSkillByTitlesCalled() {
        Mockito.doNothing().when(goalValidator)
                .validateCreationGoal(userId, maxGoal);

        Mockito.when(goalRepository.create(goalDto.tittle(),
                goalDto.description(),
                goalDto.parentId())).thenReturn(goal);

        Mockito.when(skillService.getSkillsByTitle(goalDto.titleSkills())).thenReturn(skills);

        goalServiceImpl.createGoal(userId, goalDto);

        Mockito.verify(skillService).getSkillsByTitle(goalDto.titleSkills());
    }

    @Test
    public void createGoalSaveGoalCalled() {
        Mockito.doNothing().when(goalValidator)
                .validateCreationGoal(userId, maxGoal);

        Mockito.when(goalRepository.create(goalDto.tittle(),
                goalDto.description(),
                goalDto.parentId())).thenReturn(goal);

        Mockito.when(skillService.getSkillsByTitle(goalDto.titleSkills())).thenReturn(skills);

        goalServiceImpl.createGoal(userId, goalDto);

        goal.setSkillsToAchieve(skills);
        Mockito.verify(goalRepository).save(goal);
    }

    @Test
    public void updateGoalAssignNewSkillToGoal() {
        goal.setId(1L);
        goal.setSkillsToAchieve(List.of(Skill.builder().title("skill2").build()));
        Mockito.when(goalValidator.validateUpdate(goalId, goalDto)).thenReturn(goal);

        Mockito.when(skillService.getSkillsByTitle(goalDto.titleSkills())).thenReturn(skills);
        goalServiceImpl.updateGoal(goalId, goalDto);

        assertEquals(goal.getSkillsToAchieve(), skills);
    }


    @Test
    public void deleteGoal() {
        goalServiceImpl.deleteGoal(goalId);
        Mockito.verify(goalRepository).deleteById(goalId);
    }

    @Test
    public void findSubtasksByGoalId() {
        goal.setTitle("title");
        GoalFilterDto filterDto = new GoalFilterDto();
        filterDto.setTitle("title");

        Mockito.when(goalRepository.findByParent(goalId)).thenReturn(Stream.of(goal));
        List<GoalDto> result = goalServiceImpl.findSubtasksByGoalId(goalId, filterDto);

        assertEquals(goalMapper.toDto(List.of(goal)), result);
    }

    @Test
    public void getGoalsByUser() {
        goal.setTitle("title");
        GoalFilterDto filterDto = new GoalFilterDto();
        filterDto.setTitle("title");

        Mockito.when(goalRepository.findGoalsByUserId(userId)).thenReturn(Stream.of(goal));
        List<GoalDto> result = goalServiceImpl.getGoalsByUser(userId, filterDto);

        assertEquals(goalMapper.toDto(List.of(goal)), result);
    }
}