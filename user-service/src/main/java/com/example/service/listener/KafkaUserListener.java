package com.example.service.listener;

import com.example.dto.mq_dto.RegistrationDto;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static com.example.json.JsonConverter.fromJson;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaUserListener {

    private final UserService userService;

    @KafkaListener(topics = "#{kafkaTopicConfig.getRegistrationTopic()}")
    public void createUserFromRegistrationDtoMessage(String registrationDtoMessage) {
        log.info("received registration dto {}", registrationDtoMessage);
        RegistrationDto registrationDto = fromJson(registrationDtoMessage, RegistrationDto.class);
        userService.createUserFrom(registrationDto);
    }
}
