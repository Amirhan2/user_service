package school.faang.user_service.filter.recommendation;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.RequestFilterDto;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.filter.FilterRecommendationRequest;

import java.util.stream.Stream;

@Component
public class FilterRecommendationRequestRequesterId implements FilterRecommendationRequest {
    @Override
    public boolean isApplicable(RequestFilterDto requestFilterDto) {
        return requestFilterDto.getRequesterId() != null;
    }

    @Override
    public Stream<RecommendationRequest> apply(Stream<RecommendationRequest> recommendationRequestStream, RequestFilterDto filterDto) {
        return recommendationRequestStream.filter(request -> request.getRequester().getId() == filterDto.getRequesterId());
    }
}
