package school.faang.user_service.filter.event;

import school.faang.user_service.model.event.EventFilterDto;
import school.faang.user_service.model.entity.event.Event;

import java.util.stream.Stream;

public interface EventFilter {
    boolean isApplicable(EventFilterDto filters);

    Stream<Event> apply(Stream<Event> events, EventFilterDto filters);
}
