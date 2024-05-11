package school.faang.user_service.filter;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.goal.InvitationFilterDto;
import school.faang.user_service.entity.RequestStatus;
import school.faang.user_service.service.filter.InvitedNamePatternFilter;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
@ExtendWith(MockitoExtension.class)
public class InvitedNamePatternFilterTest {
    @InjectMocks
    InvitedNamePatternFilter invitedNamePatternFilter;
    InvitationFilterDto invitationFilterDto;
    Data data;

    @BeforeEach
    void prepareInvitationFilterDto() {
        invitationFilterDto = new InvitationFilterDto();
        invitationFilterDto.setInvitedId(2L);
        invitationFilterDto.setInviterId(1L);
        invitationFilterDto.setInviterNamePattern("John");
        invitationFilterDto.setInvitedNamePattern("Mike");
        invitationFilterDto.setStatus(RequestStatus.ACCEPTED);
        data = new Data();
    }


    @Test
    void testIsApplicableFalse() {
        invitationFilterDto.setInvitedNamePattern(null);
        assertFalse(invitedNamePatternFilter.isApplicable(invitationFilterDto));
    }

    @Test
    void testIsApplicableTrue() {
        assertTrue(invitedNamePatternFilter.isApplicable(invitationFilterDto));
    }

    @Test
    void testApplyWithGoalInvitation() {
        assertEquals(data.prepareGoalInvitationStream().toList().size(), invitedNamePatternFilter.apply(data.prepareGoalInvitationStream(),
                invitationFilterDto).toList().size());
    }

    @Test
    void testApplyWithoutGoalInvitation() {
        invitationFilterDto.setInvitedNamePattern("Kelly");
        assertEquals(0, invitedNamePatternFilter.apply(data.prepareGoalInvitationStream(), invitationFilterDto).toList().size());
    }
}
