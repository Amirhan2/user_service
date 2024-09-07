package school.faang.user_service.dto.recomendation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateRecommendationRequestDto {
    @NotBlank(message = "Message can't be blank or empty")
    @Length(max = 4096, message = "Maximum number of characters")
    private String message;
    @NotEmpty(message = "Skills can't be empty")
    private List<Long> skills;
    @NotNull(message = "Requester Id can't be empty")
    private Long requesterId;
    @NotNull(message = "Receiver Id can't be empty")
    private Long receiverId;
}