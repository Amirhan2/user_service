package school.faang.user_service.validation.event;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.SkillDto;
import school.faang.user_service.dto.event.EventDto;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.repository.event.EventRepository;
import school.faang.user_service.service.SkillService;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class EventValidator {
    private final EventRepository eventRepository;
    private final SkillService skillService;

    //TODO Murzin34 Задействовать обработчик ошибок
    public void eventDatesValidation(EventDto eventDto) {
        if (eventDto.getStartDate().isAfter(eventDto.getEndDate())) {
            throw new DataValidationException("Start_date cannot be after end_date.");
        }
    }

    public void relatedSkillsValidation(EventDto eventDto) {
        Set<SkillDto> ownerSkills = new HashSet<>(skillService.getUserSkillDtoList(eventDto.getOwnerId()));
        if (!ownerSkills.containsAll(eventDto.getRelatedSkills())) {
            throw new DataValidationException("Owner must have valid skills.");
        }
    }

    public void eventExistByIdValidation(Long id) {
        if (!eventRepository.existsById(id)) {
            throw new DataValidationException("Event by ID: " + id + " dont exist.");
        }
    }
}
