package school.faang.user_service.service.event.event_filters;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.event.EventFilterDto;
import school.faang.user_service.entity.event.Event;

import java.util.stream.Stream;

@Component
public class EventOwnerNameFilter implements EventFilter {

    @Override
    public boolean isApplicable(EventFilterDto filters) {
        return filters.getOwnerName() != null && !filters.getOwnerName().isEmpty();
    }

    @Override
    public Stream<Event> apply(Stream<Event> events, EventFilterDto filters) {
        String ownerNamePattern = filters.getOwnerName();
        return events.filter(event -> event.getOwner() != null
                && event.getOwner().getUsername() != null
                && event.getOwner().getUsername().contains(ownerNamePattern));
    }
}