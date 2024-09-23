package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.recommendation.RecommendationDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.recommendation.SkillOffer;
import school.faang.user_service.entity.UserSkillGuarantee;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillOfferService {

    private final SkillOfferRepository skillOfferRepository;
    private final UserSkillGuaranteeService userSkillGuaranteeService;
    private final SkillService skillService;
    private final UserService userService;

    public List<SkillOffer> findAllOffersOfSkill(Skill skill, User user) {
        return skillOfferRepository.findAllOffersOfSkill(skill.getId(), user.getId());
    }

    public void addSkillsWithGuarantees(List<Skill> skills, Long recommendationId, RecommendationDto recommendationDto) {
        User receiver = userService.findById(recommendationDto.getReceiverId());
        User author = userService.findById(recommendationDto.getAuthorId());

        for (Skill skill : skills) {
            skillOfferRepository.create(skill.getId(), recommendationId);

            if (receiver.getSkills().contains(skill)) {
                UserSkillGuarantee existedGuarantee = userSkillGuaranteeService
                        .saveUserSkillGuarantee(skill, receiver, author);

                skill.getGuarantees().add(existedGuarantee);
                skillService.saveSkill(skill);
            }
        }
    }

    public void deleteAllByRecommendationId(Long id) {
        skillOfferRepository.deleteAllByRecommendationId(id);
    }
}
