package school.faang.user_service.dto;

import jakarta.validation.constraints.NotBlank;

public record RejectionDto(@NotBlank(message = "There must be a reason for rejection.") String reason) {
}
