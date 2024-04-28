package school.faang.user_service.config.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import school.faang.user_service.listener.BanUserListener;

@Configuration
@RequiredArgsConstructor
public class RedisListenerConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.channels.user_ban_channel.name}")
    private String userBanTopic;

    private final BanUserListener banUserListener;

    @Bean
    public ChannelTopic banUserTopic() {
        return new ChannelTopic(userBanTopic);
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public MessageListenerAdapter banUserListenerAdapter() {
        return new MessageListenerAdapter(banUserListener);
    }

    @Bean
    public RedisMessageListenerContainer banUserMessageListenerContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(banUserListenerAdapter(), banUserTopic());
        return container;
    }
}
