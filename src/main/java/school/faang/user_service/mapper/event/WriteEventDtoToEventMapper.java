package school.faang.user_service.mapper.event;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mappings;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import school.faang.user_service.dto.event.WriteEventDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.event.Event;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserRepository;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class WriteEventDtoToEventMapper {

    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected SkillRepository skillRepository;

    @Mappings({
            @Mapping(target = "owner", expression = "java(findUserById(writeEventDto.getOwnerId()))"),
            @Mapping(target = "relatedSkills", expression = "java(findSkillsByIds(writeEventDto.getRelatedSkillIds()))")
    })
    public abstract Event map(WriteEventDto writeEventDto);

    @Mappings({
            @Mapping(target = "owner", expression = "java(findUserById(writeEventDto.getOwnerId()))"),
            @Mapping(target = "relatedSkills", expression = "java(findSkillsByIds(writeEventDto.getRelatedSkillIds()))")
    })
    public abstract Event map(WriteEventDto writeEventDto, @MappingTarget Event event);

    protected User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    protected List<Skill> findSkillsByIds(List<Long> ids) {
        return skillRepository.findAllById(ids);
    }
}