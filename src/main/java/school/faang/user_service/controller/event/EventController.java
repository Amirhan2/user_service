package school.faang.user_service.controller.event;

import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.event.EventDto;
import school.faang.user_service.dto.event.filters.EventFilterDto;
import school.faang.user_service.service.event.EventService;

import java.util.List;

@RequestMapping("/events")
@RestController
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;

    //TODO Murzin34 Перенести маппинг из сервиса в контроллер.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDto createEvent(@RequestBody @Valid EventDto eventDto) {
        return eventService.createEvent(eventDto);
    }

    @GetMapping("/{eventId}")
    public EventDto getEvent(@PathVariable long eventId) {
        return eventService.getEvent(eventId);
    }

    @PostMapping("/filters")
    public List<EventDto> getEventsByFilter(@RequestBody @NonNull EventFilterDto eventFilterDto) {
        return eventService.getEventsByFilters(eventFilterDto);
    }

    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable long eventId) {
        eventService.deleteEvent(eventId);
    }

    @PatchMapping
    public EventDto updateEvent(@RequestBody @Valid EventDto eventDto) {
        return eventService.updateEvent(eventDto);
    }

    @GetMapping("/owner")
    public List<EventDto> getEventsOwner(@RequestParam long ownerId) {
        return eventService.getEventsOwner(ownerId);
    }

    @GetMapping("/participant")
    public List<EventDto> getEventParticipants(@RequestParam long participantsId) {
        return eventService.getEventParticipants(participantsId);
    }
}
