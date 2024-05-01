package school.faang.user_service.controller.event;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.event.EventDto;
import school.faang.user_service.dto.event.EventFilterDto;
import school.faang.user_service.dto.skill.SkillDto;
import school.faang.user_service.exceptions.event.DataValidationException;
import school.faang.user_service.service.event.EventService;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {
    @Mock
    private EventService service;
    @InjectMocks
    private EventController controller;
    private EventDto event;

    @BeforeEach
    void init() {
        List<SkillDto> skillDtoList = List.of(
                new SkillDto(1L, "skill1"),
                new SkillDto(2L, "skill2")
        );
        event = EventDto.builder()
                .ownerId(1L)
                .title("dto")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusDays(1L))
                .relatedSkills(skillDtoList)
                .build();
    }

    @Test
    void createNullEvent() {
        assertThrows(NullPointerException.class, () -> controller.create(null));
    }

    @Test
    void createNullTitleEvent() {
        event.setTitle(null);
        DataValidationException e = assertThrows(DataValidationException.class, () -> controller.create(event));
        assertEquals("not valid event - " + event, e.getMessage());
    }

    @Test
    void createBlankTitleEvent() {
        event.setTitle("");
        DataValidationException e = assertThrows(DataValidationException.class, () -> controller.create(event));
        assertEquals("not valid event - " + event, e.getMessage());
    }

    @Test
    void createNullStartDateEvent() {
        event.setStartDate(null);
        DataValidationException e = assertThrows(DataValidationException.class, () -> controller.create(event));
        assertEquals("not valid event - " + event, e.getMessage());
    }

    @Test
    void createNullOwnerIdEvent() {
        event.setOwnerId(null);
        DataValidationException e = assertThrows(DataValidationException.class, () -> controller.create(event));
        assertEquals("not valid event - " + event, e.getMessage());
    }

    @Test
    void createGoodEvent() {
        when(service.create(event)).thenReturn(event);
        assertEquals(event, controller.create(event));
    }

    @Test
    void getEvent() {
        when(service.getEvent(1L)).thenReturn(event);
        assertEquals(event, controller.getEvent(1L));
    }

    @Test
    void getEventByFilter() {
        EventFilterDto filterDto = new EventFilterDto();
        when(service.getEventsByFilter(filterDto)).thenReturn(List.of(event));
        assertIterableEquals(List.of(event), controller.getEventsByFilter(filterDto));
    }

    @Test
    void deleteEvent() {
        when(service.deleteEvent(1L)).thenReturn(event);
        assertEquals(event, controller.deleteEvent(1L));
    }

    @Test
    void updateBadEvent() {
        event.setOwnerId(null);
        DataValidationException e = assertThrows(DataValidationException.class, () -> controller.updateEvent(event));
        assertEquals("not valid event - " + event, e.getMessage());
    }

    @Test
    void updateGoodEvent() {
        when(service.updateEvent(event)).thenReturn(event);
        assertEquals(event, controller.updateEvent(event));
    }

    @Test
    void getOwnedEvents() {
        when(service.getOwnedEvents(1L)).thenReturn(List.of(event));
        assertIterableEquals(List.of(event), controller.getOwnedEvents(1L));
    }

    @Test
    void getParticipatedEvents() {
        when(service.getParticipatedEvents(1L)).thenReturn(List.of(event));
        assertIterableEquals(List.of(event), controller.getParticipatedEvents(1L));
    }
}