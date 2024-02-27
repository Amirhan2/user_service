package school.faang.user_service.filter.event;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.event.EventFilterDto;
import school.faang.user_service.entity.event.Event;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
public class EventEndDatePattern implements EventFilter {
    @Override
    public boolean isApplicable(EventFilterDto filters) {
        return filters.getEndDatePattern() != null;
    }

    @Override
    public Stream<Event> apply(Stream<Event> events, EventFilterDto filters) {
        LocalDateTime endDatePattern = filters.getEndDatePattern();
        return events.filter(event ->
                !event.getEndDate().isBefore(endDatePattern));
    }
}
