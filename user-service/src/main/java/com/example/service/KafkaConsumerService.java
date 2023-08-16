package com.example.service;

import com.example.dto.kafka_message.KafkaMessage;
import com.example.dto.response.UserDto;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.example.json.JsonMapper.fromJson;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final UserService userService;

    @KafkaListener(topics = "${kafka.topics.user-registration}")
    void createUserFromRegistrationMessage(String message) {
        log.info("received new user registration message: {}", message);
        KafkaMessage<UserDto> kafkaMessage = fromJson(message, new TypeToken<>() {});
        userService.createUserFrom(kafkaMessage.content());
    }
}
