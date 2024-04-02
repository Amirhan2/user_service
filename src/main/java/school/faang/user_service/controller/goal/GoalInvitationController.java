package school.faang.user_service.controller.goal;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.goal.GoalInvitationDto;
import school.faang.user_service.dto.goal.filter.GoalInvitationFilterDto;
import school.faang.user_service.service.goal.GoalInvitationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/invitations")
public class GoalInvitationController {
    private final GoalInvitationService goalInvitationService;

    @PostMapping()
    @Operation(description = "Creating an invitation")
    public GoalInvitationDto createInvitation(@RequestBody GoalInvitationDto invitation) {
        return goalInvitationService.createInvitation(invitation);
    }

    @GetMapping("/accept/{id}")
    @Operation(description = "Creating an invitation")
    public GoalInvitationDto acceptGoalInvitation(@PathVariable long id) {
        return goalInvitationService.acceptGoalInvitation(id);
    }


    @GetMapping("/reject/{id}")
    @Operation(description = "Decline the invitation")
    public GoalInvitationDto rejectGoalInvitation(@PathVariable long id) {
        return goalInvitationService.rejectGoalInvitation(id);
    }

    @PostMapping("/get")
    @Operation(description = "Filter out invitations")
    public List<GoalInvitationDto> getInvitations(@RequestBody GoalInvitationFilterDto filter) {
        return goalInvitationService.getFilteredInvitations(filter);
    }
}