package school.faang.user_service.filter.invitation;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.goal.InvitationFilterDto;
import school.faang.user_service.entity.goal.GoalInvitation;

import java.util.stream.Stream;

@Component
public class InvitationInvitedIdNameFilter implements InvitationFilter {
    @Override
    public boolean isAcceptable(InvitationFilterDto goal) {
        return goal.getInvitedId() != null;
    }

    @Override
    public Stream<GoalInvitation> apply(Stream<GoalInvitation> goal, InvitationFilterDto filters) {
        return goal.filter(invintation -> invintation.getInvited().getId() == (filters.getInvitedId()));
    }
}