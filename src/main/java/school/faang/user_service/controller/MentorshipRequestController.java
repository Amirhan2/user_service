package school.faang.user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import school.faang.user_service.dto.filter.RequestFilterDto;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.mentorship.MentorshipRequestDto;
import school.faang.user_service.service.mentorship.MentorshipRequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MentorshipRequestController {

    private final MentorshipRequestService mentorshipRequestService;

    @PostMapping("/mentorship/request")
    @ResponseStatus(HttpStatus.CREATED)
    public MentorshipRequestDto requestMentorship(@Valid @RequestBody MentorshipRequestDto mentorshipRequestDto) {
        return mentorshipRequestService.requestMentorship(mentorshipRequestDto);
    }

    @GetMapping("/mentorship/request")
    @ResponseStatus(HttpStatus.OK)
    public List<MentorshipRequestDto> getRequests(@Valid @RequestBody RequestFilterDto requestFilterDto) {
        return mentorshipRequestService.getRequests(requestFilterDto);
    }

    @PutMapping("/mentorship/request/{id}/accept")
    @ResponseStatus(HttpStatus.OK)
    public MentorshipRequestDto acceptRequest(@PathVariable Long id) {
        return mentorshipRequestService.acceptRequest(id);
    }
}
