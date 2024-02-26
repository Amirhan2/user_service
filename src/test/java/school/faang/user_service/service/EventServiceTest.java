package school.faang.user_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import school.faang.user_service.entity.event.Event;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.repository.event.EventRepository;
import school.faang.user_service.service.event.EventService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventService eventService;

    @Test
    @DisplayName("Успешное получение всех событий по верному Id пользователя")
    public void testSuccessGetEventById() {
        Event event1 = Event.builder()
                .id(21L)
                .title("EventOne")
                .maxAttendees(2)
                .build();
        Event event2 = Event.builder()
                .id(22L)
                .title("EventTwo")
                .maxAttendees(2)
                .build();
        User user1 = User.builder().id(21L).active(true).build();
        long userId = user1.getId();
        List<User> attendees = new ArrayList<>();
        attendees.add(user1);
        event1.setAttendees(attendees);
        event2.setAttendees(attendees);
        List<Event> events = new ArrayList<>(List.of(event1, event2));

        verify(eventRepository, times(1)).findParticipatedEventsByUserId(userId);
        assertEquals(mockEvents, events);

    }

    @Test
    @DisplayName("Неуспешный поиск события по неверному Id")
    public void testFailedGetEventByIncorrectId() {
        long wrongId = 11L;
        Mockito.when(eventRepository.findById(wrongId)).thenReturn(Optional.empty());

        assertThrows(DataValidationException.class, () -> eventService.getEvent(wrongId));
    }

    @Test
    @DisplayName("Успешное удаление события по верному Id")
    public void testSuccessDeleteEventById() {
        Event eventDelete = Event.builder()
                .id(5L)
                .maxAttendees(5)
                .build();
        long eventId = eventDelete.getId();

        eventService.deleteEvent(eventId);
        Mockito.verify(eventRepository, times(1)).deleteById(eventId);
    }

    @Test
    @DisplayName("Неуспешное удаление события по неверному Id")
    public void testFailedDeleteEventByIncorrectId() {
        long wrongId = 15L;

        Mockito.verify(eventRepository, Mockito.never()).deleteById(wrongId);
    }

}
