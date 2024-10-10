package school.faang.user_service.dto.recommendation;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RecommendationDto {
    private Long id;
    private Long authorId;
    private Long receiverId;
    @NotBlank(message = "Recommendation text cannot be empty")
    private String content;
    private List<SkillOfferDto> skillOffers;
    private LocalDateTime createdAt;
}
