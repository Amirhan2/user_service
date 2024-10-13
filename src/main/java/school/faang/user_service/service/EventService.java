package school.faang.user_service.service;

import school.faang.user_service.dto.event.EventDto;
import school.faang.user_service.dto.event.EventFilterDto;
import school.faang.user_service.entity.event.Event;

import java.util.List;

public interface EventService {
    EventDto create(EventDto event);

    EventDto getEvent(Long eventId);

    List<EventDto> getEventsByFilter(EventFilterDto filterDto);

    void deleteEvent(Long eventId);

    void deleteEvents(List<Event> events);

    void deletePassedEvents();

    void deletePassedEventsByBatches(List<Event> events);

    EventDto updateEvent(EventDto eventDto);

    List<Event> getOwnedEvents(Long userId);

    List<Event> getParticipatedEvents(Long userId);

    void findEventsStartingRightNow();
}
