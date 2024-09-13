package school.faang.user_service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.SkillDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.test_data.event.TestDataEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class SkillCustomMapperTest {
    @InjectMocks
    private SkillCustomMapper skillCustomMapper;
    private TestDataEvent testDataEvent;
    private Skill skill;
    private SkillDto skillDto;

    @BeforeEach
    public void init() {
        testDataEvent = new TestDataEvent();
    }

    @Test
    void testToDto_Success() {
        skill = testDataEvent.getSkill1();
        skillDto = new SkillDto();

        skillDto = skillCustomMapper.toDto(skill);

        assertNotNull(skillDto);
        assertThat(skillDto).usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(skill);
    }

    @Test
    void testToEntity_Success() {
        skill = new Skill();
        skillDto = testDataEvent.getSkillDto1();

        skill = skillCustomMapper.toEntity(skillDto);

        assertNotNull(skill);
        assertThat(skill).usingRecursiveComparison()
                .ignoringActualNullFields()
                .isEqualTo(skillDto);
    }
}