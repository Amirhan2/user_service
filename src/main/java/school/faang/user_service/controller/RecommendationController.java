package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.recommendation.RecommendationDto;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.exception.ErrorMessage;
import school.faang.user_service.service.RecommendationService;

import java.util.List;

@Validated
@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService recommendationService;

    @PostMapping("/give")
    public RecommendationDto giveRecommendation(@RequestBody RecommendationDto recommendationDto) {
        validateRecommendation(recommendationDto);
        return recommendationService.create(recommendationDto);
    }

    @PutMapping("/update/{id}")
    public RecommendationDto updateRecommendation(@PathVariable long id, @RequestBody RecommendationDto updatedRecommendationDto) {
        validateRecommendation(updatedRecommendationDto);
        return recommendationService.update(id, updatedRecommendationDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRecommendation(@PathVariable Long id) {
        recommendationService.delete(id);
    }

    @GetMapping("/user/{receiverId}")
    public List<RecommendationDto> getAllUserRecommendations(@PathVariable long receiverId) {
        return recommendationService.getAllUserRecommendations(receiverId);
    }

    @GetMapping("/user/{authorId}")
    public List<RecommendationDto> getAllGivenRecommendations(@PathVariable long authorId) {
        return recommendationService.getAllGivenRecommendations(authorId);
    }

    private void validateRecommendation(RecommendationDto recommendationDto) {
        if (recommendationDto.getContent() == null || recommendationDto.getContent().isBlank()) {
            throw new DataValidationException(ErrorMessage.RECOMMENDATION_CONTENT);
        }
        if (recommendationDto.getAuthorId() == null) {
            throw new DataValidationException(ErrorMessage.RECOMMENDATION_AUTHOR);
        }
        if (recommendationDto.getReceiverId() == null) {
            throw new DataValidationException(ErrorMessage.RECOMMENDATION_RECEIVER);
        }
    }
}
