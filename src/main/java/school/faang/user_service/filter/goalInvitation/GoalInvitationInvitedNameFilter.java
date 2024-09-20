package school.faang.user_service.filter.goalInvitation;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.GoalInvitationFilterDto;
import school.faang.user_service.entity.goal.GoalInvitation;

import java.util.stream.Stream;

@Component
class GoalInvitationInvitedNameFilter implements GoalInvitationFilter {
    @Override
    public boolean isApplicable(GoalInvitationFilterDto goalInvitationFilterDto) {
        return goalInvitationFilterDto.getInvitedNamePattern() != null &&
                !goalInvitationFilterDto.getInvitedNamePattern().isBlank();
    }

    @Override
    public Stream<GoalInvitation> apply(Stream<GoalInvitation> goalInvitationStream,
                                        GoalInvitationFilterDto goalInvitationFilterDto) {
        return goalInvitationStream.filter(goalInvitation -> goalInvitation.getInvited() != null &&
                goalInvitation.getInvited().getUsername().contains(goalInvitationFilterDto.getInvitedNamePattern()));
    }
}