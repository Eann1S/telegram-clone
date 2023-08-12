package com.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {

    private final KafkaMessageProcessor messageProcessor;

    @KafkaListener(topics = "${kafka.topics.user-registration}")
    void receiveUserRegistrationMessage(String message) {
        log.info("received new user registration message: {}", message);
        messageProcessor.processUserRegistrationMessage(message);
    }
}
