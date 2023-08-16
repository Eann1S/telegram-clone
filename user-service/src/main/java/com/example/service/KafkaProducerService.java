package com.example.service;

import com.example.dto.kafka_message.KafkaMessage;
import com.example.dto.response.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.example.json.JsonMapper.toJson;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    @Value("${kafka.topics.user-update}")
    private String userUpdateTopic;

    public void sendUserUpdateMessage(UserDto userDto) {
        KafkaMessage<UserDto> kafkaMessage = KafkaMessage.of(userDto);
        kafkaTemplate.send(userUpdateTopic, toJson(kafkaMessage));
        log.info("{} was sent to user update topic", kafkaMessage);
    }
}
