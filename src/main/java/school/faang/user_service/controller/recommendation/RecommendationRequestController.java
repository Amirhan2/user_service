package school.faang.user_service.controller.recommendation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.dto.RejectionDto;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.service.RecommendationRequestService;
import school.faang.user_service.service.filter.recommendation.RequestFilterDto;

import java.util.List;

@Component
public class RecommendationRequestController {

    private final RecommendationRequestService service;

    @Autowired
    public RecommendationRequestController(RecommendationRequestService service) {
        this.service = service;
    }

    public RecommendationRequestDto requestRecommendation(RecommendationRequestDto dto) {
        if (dto.getMessage() == null) {
            throw new RuntimeException("Сообщение не может быть пустым");
        }
        return service.create(dto);
    }

    public List<RecommendationRequestDto> getRecommendationRequests(RequestFilterDto filter) {
        return service.getRequests(filter);
    }

    public RecommendationRequestDto getRecommendationRequest(long id) {
        return service.getRequest(id);
    }

    public void rejectRequest(long id, RejectionDto rejection) {
        service.rejectRequest(id, rejection);
    }

}
