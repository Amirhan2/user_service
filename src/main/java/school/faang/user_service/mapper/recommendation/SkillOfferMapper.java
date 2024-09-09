package school.faang.user_service.mapper.recommendation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import school.faang.user_service.dto.recommendation.SkillOfferDto;
import school.faang.user_service.entity.recommendation.SkillOffer;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillOfferMapper {
    @Mapping(target = "skillId", source = "skill.id")
    @Mapping(target = "recommendationId", source = "recommendation.id")
    SkillOfferDto toDto(SkillOffer skillOffer);

    @Mapping(target = "skill.id", ignore = true)
    @Mapping(target = "recommendation.id", ignore = true)
    SkillOffer toEntity(SkillOfferDto skillOfferDto);
}
