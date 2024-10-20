package school.faang.user_service.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import school.faang.user_service.model.event.MentorshipRequestedEvent;

@Component
@RequiredArgsConstructor
public class MentorshipRequestedEventPublisher {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic mentorshipRequestTopic;

    public void publish(MentorshipRequestedEvent event) {
        redisTemplate.convertAndSend(mentorshipRequestTopic.getTopic(), event);
    }
}
