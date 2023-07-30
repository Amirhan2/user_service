package school.faang.user_service.service.mentorship.filter;

import school.faang.user_service.dto.mentorship.RequestFilterDto;
import school.faang.user_service.entity.MentorshipRequest;

import java.util.stream.Stream;

public class MentorshipRequestFilterByRequester implements MentorshipRequestFilter {

    @Override
    public boolean isApplicable(RequestFilterDto filter) {
        return filter.getRequester() > 0;
    }

    @Override
    public Stream<MentorshipRequest> apply(Stream<MentorshipRequest> stream, RequestFilterDto filter) {
        return stream.filter(request -> request.getRequester().getId() == filter.getRequester());
    }
}
