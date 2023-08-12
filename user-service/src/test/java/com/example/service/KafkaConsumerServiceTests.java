package com.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTests {

    @Mock
    private KafkaMessageProcessor messageProcessor;
    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Test
    void shouldReceiveUserRegistrationMessage() {
        kafkaConsumerService.receiveUserRegistrationMessage("message");

        verify(messageProcessor).processUserRegistrationMessage("message");
    }
}