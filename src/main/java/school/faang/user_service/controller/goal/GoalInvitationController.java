package school.faang.user_service.controller.goal;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.goal.GoalInvitationDto;
import school.faang.user_service.service.GoalInvitationService;

@RestController
@RequestMapping("/goal/invitation")
@RequiredArgsConstructor
public class GoalInvitationController {
    private final GoalInvitationService goalInvitationService;

    @PostMapping
    public ResponseEntity<GoalInvitationDto> createInvitation(GoalInvitationDto invitation) {
        return ResponseEntity.ok(goalInvitationService.createInvitation(invitation));
    }

    @PatchMapping("/accept")
    public ResponseEntity<GoalInvitationDto> acceptGoalInvitation(long id) {
        return ResponseEntity.ok(goalInvitationService.acceptGoalInvitation(id));
    }
    @PatchMapping("/reject")
    public ResponseEntity<GoalInvitationDto> rejectGoalInvitation(long id) {
        return ResponseEntity.ok(goalInvitationService.rejectGoalInvitation(id));
    }

}
