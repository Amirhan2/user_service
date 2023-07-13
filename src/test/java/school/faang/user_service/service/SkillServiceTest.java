package school.faang.user_service.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.entity.dto.skill.SkillDto;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.mapper.SkillMapper;
import school.faang.user_service.repository.SkillRepository;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private SkillMapper skillMapper;

    @InjectMocks
    private SkillService skillService;

    @Test
    void testExistsByTitle() {
        Mockito.when(skillRepository.existsByTitle("crek")).thenReturn(true);

        assertThrows(
                DataValidationException.class,
                () -> skillService.create(new SkillDto(1L, "crek"))
        );
    }

    @Test
    void testCreate() {
        SkillDto skillDto = new SkillDto(1L, "crek");
        skillService.create(skillDto);
        Mockito.verify(skillRepository, Mockito.times(1))
                .save(skillMapper.toEntity(skillDto));
    }

    @Test
    void testGetUserSkills() {
        Mockito.when(skillRepository.findAllByUserId(1L)).thenReturn(Mockito.anyList());

        skillService.getUserSkills(1L);

        Mockito.verify(skillRepository, Mockito.times(1))
                .findAllByUserId(1L);
    }

    @Test
    void testGetOfferedSkills(){
        Mockito.when(skillRepository.findAllByUserId(1L)).thenReturn(Mockito.anyList());

        skillService.getOfferedSkills(1L);

        Mockito.verify(skillRepository, Mockito.times(1))
                .findAllByUserId(1L);
    }
}