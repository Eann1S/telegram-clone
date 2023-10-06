package com.example.service.messaging;

import com.example.config.kafka.KafkaTopicConfig;
import com.example.dto.mq_dto.UpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.example.json.JsonConverter.toJson;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaJsonUserMessagingService implements UserMessagingService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaTopicConfig kafkaTopicConfig;

    @Override
    public void send(UpdateDto updateDto) {
        String userUpdateTopic = kafkaTopicConfig.getUserUpdateTopic();
        kafkaTemplate.send(userUpdateTopic, toJson(updateDto));
        log.info("{} was sent to {}", updateDto, userUpdateTopic);
    }
}
