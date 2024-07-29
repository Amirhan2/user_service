package school.faang.user_service.mapper;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import school.faang.user_service.dto.event.EventDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.event.Event;
import school.faang.user_service.service.user.UserService;

@Mapper(componentModel = "spring",
        uses = {SkillMapper.class, UserService.class},
        unmappedTargetPolicy = org.mapstruct.ReportingPolicy.IGNORE)
public interface EventMapper {
    @Mapping(source = "owner.id", target = "ownerId")
    EventDto toDto(Event event);

    @Mapping(source = "ownerId", target = "owner", qualifiedByName = "findUserById")
    Event toEntity(EventDto eventDto, @Context UserService userService);

    @Named("findUserById")
    default User findUserById(Long id, @Context UserService userService) {
        return userService.findUserById(id);
    }
}