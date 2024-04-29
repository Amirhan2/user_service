package school.faang.user_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import school.faang.user_service.dto.skill.SkillCandidateDto;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.UserSkillGuarantee;
import school.faang.user_service.entity.recommendation.SkillOffer;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.mapper.SkillCandidateMapper;
import school.faang.user_service.mapper.SkillMapper;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserSkillGuaranteeRepository;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SkillService {

    private final static long MIN_SKILL_OFFERS = 3;
    private final SkillRepository skillRepository;
    private final SkillOfferRepository skillOfferRepository;
    private final SkillMapper skillMapper;
    private final SkillCandidateMapper skillCandidateMapper;
    private final UserSkillGuaranteeRepository userSkillGuaranteeRepository;

    @Transactional
    public SkillDto create(SkillDto skillDto) {
        if (skillRepository.existsByTitle(skillDto.getTitle())) {
            String error = "Skill with title: '" + skillDto.getTitle() + "' already exists in DB";
            log.error(error);
            throw new DataValidationException(error);
        }
        return Optional.of(skillDto)
                .map(skillMapper::dtoToSkill)
                .map(skillRepository::save)
                .map(skillMapper::skillToDto)
                .orElseThrow();
    }

    @Transactional(readOnly = true)
    public List<SkillDto> getUserSkills(long userId) {
        return skillRepository.findAllByUserId(userId).stream()
                .map(skillMapper::skillToDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<SkillCandidateDto> getOfferedSkills(long userId) {
        return skillRepository.findSkillsOfferedToUser(userId).stream()
                .collect(Collectors.groupingBy(skill -> skill, Collectors.counting()))
                .entrySet().stream()
                .map(entry -> skillCandidateMapper.skillToCandidateDto(entry.getKey(), entry.getValue()))
                .toList();
    }

    @Transactional
    public SkillDto acquireSkillFromOffers(long skillId, long userId) {
        Optional<Skill> userSkill = skillRepository.findUserSkill(skillId, userId);
        if (userSkill.isPresent()) {
            throw new DataValidationException("User " + userId + " already has skill with ID: " + skillId);
        }

        log.info("Find all skill offers for skill {} and user {}", skillId, userId);
        List<SkillOffer> skillOfferList = skillOfferRepository.findAllOffersOfSkill(skillId, userId);

        int countOffersSkill = skillOfferList.size();
        if (countOffersSkill < MIN_SKILL_OFFERS) {
            throw new DataValidationException("Skill with ID: " + skillId + " hasn't enough offers for user with ID: " + userId);
        }

        log.info("Add skill with ID: {} to user with ID: {}", skillId, userId);
        skillRepository.assignSkillToUser(skillId, userId);

        //Create guarantors for user skill
        Skill skill = skillOfferList.get(0).getSkill();
        skillOfferList.stream()
                .map(SkillOffer::getRecommendation)
                .forEach(recommendation -> {
                    log.info("Create guarantee user: {} for user: {} for skill: {}",
                            recommendation.getAuthor().getUsername(), recommendation.getReceiver().getUsername(), skill.getTitle());
                    userSkillGuaranteeRepository
                            .save(new UserSkillGuarantee(null, recommendation.getReceiver(), skill, recommendation.getAuthor()));
                });

        return skillMapper.skillToDto(skill);
    }
}
