package school.faang.user_service.controller.goal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.service.GoalInvitationService;

@Component
@RequiredArgsConstructor
public class GoalInvitationController {
    private final GoalInvitationService goalInvitationService;

    public void rejectGoalInvitation(long id) {
        goalInvitationService.rejectGoalInvitation(id);
    }
}
