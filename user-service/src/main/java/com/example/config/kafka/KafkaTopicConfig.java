package com.example.config.kafka;

import lombok.Getter;
import lombok.Setter;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "kafka.topics")
public class KafkaTopicConfig {

    private String registrationTopic;
    private String userUpdateTopic;

    @Bean
    public NewTopic createUserUpdateTopic() {
        return new NewTopic(userUpdateTopic, 1, (short) 1);
    }
}
