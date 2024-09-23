package school.faang.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import school.faang.user_service.dto.GoalInvitationDto;
import school.faang.user_service.entity.goal.GoalInvitation;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GoalInvitationMapper {
    GoalInvitationMapper INSTANCE = Mappers.getMapper(GoalInvitationMapper.class);
    GoalInvitation toEntity(GoalInvitationDto invitationDto);
}
