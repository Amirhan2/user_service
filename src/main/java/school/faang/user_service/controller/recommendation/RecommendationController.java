package school.faang.user_service.controller.recommendation;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.recommendation.RecommendationDto;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.service.recommendation.RecommendationService;

@RestController
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationDto giveRecommendation(RecommendationDto recommendation) {
        validateRecommendationContent(recommendation);

        return recommendationService.create(recommendation);
    }

    public RecommendationDto updateRecommendation(RecommendationDto recommendation) {
        validateRecommendationContent(recommendation);

        return recommendationService.update(recommendation);
    }

    private void validateRecommendationContent(RecommendationDto recommendationDto) {
        String content = recommendationDto.getContent();

        if (content == null || content.isBlank()) {
            throw new DataValidationException("Content can't be empty");
        }
    }
}
