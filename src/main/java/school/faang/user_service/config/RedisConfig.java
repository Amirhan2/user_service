package school.faang.user_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import school.faang.user_service.dto.MessageEvent;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channels.mentorship_requested_channel.name}")
    private String mentorshipRequestedChannel;

    @Value("${spring.data.redis.channels.mentorship_accepted_channel.name}")
    private String mentorshipAcceptedChannel;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       ObjectMapper mapper) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(mapper, MessageEvent.class));
        return redisTemplate;
    }

    @Bean
    ChannelTopic topic() {
        return new ChannelTopic(mentorshipRequestedChannel);
    }

    @Bean
    ChannelTopic mentorshipAcceptedTopic() {
        return new ChannelTopic(mentorshipAcceptedChannel);
    }

    @Bean
    ChannelTopic mentorshipRequestedChannel() {
        return new ChannelTopic(mentorshipRequestedChannel);
    }
}
