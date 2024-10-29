package school.faang.user_service.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import school.faang.user_service.entity.RequestStatus;

@Getter
@Setter
@NoArgsConstructor
public class RequestFilterDto {
    private Long id;
    private Long requesterId;
    private Long mentorId;
    private String description;
    private RequestStatus status;
}
