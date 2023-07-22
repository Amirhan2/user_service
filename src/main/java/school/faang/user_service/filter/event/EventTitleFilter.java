package school.faang.user_service.filter.event;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.event.EventFilterDto;
import school.faang.user_service.entity.event.Event;

@Component
public class EventTitleFilter extends EventFilter {
    @Override
    protected boolean applyFilter(Event event, EventFilterDto filter) {
        return event.getTitle().contains(filter.getTitlePattern());
    }

    @Override
    public boolean isApplicable(EventFilterDto filter) {
        return filter.getTitlePattern() != null;
    }
}
