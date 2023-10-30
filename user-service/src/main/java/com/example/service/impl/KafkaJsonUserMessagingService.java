package com.example.service.impl;

import com.example.config.kafka.KafkaTopicConfig;
import com.example.dto.mq_dto.UpdateDto;
import com.example.service.UserMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.example.json.JsonConverter.toJson;

@Service
@Qualifier("kafka")
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
