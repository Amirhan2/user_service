package school.faang.user_service.dto.goal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.faang.user_service.entity.RequestStatus;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@NotNull
public class GoalInvitationDto {
    private Long id;
    private Long inviterId;
    private Long invitedUserId;
    private Long goalId;
    private RequestStatus status;
}