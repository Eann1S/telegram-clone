package com.example.service;

import com.example.dto.response.UserDto;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;
import static util.TestGsonMapper.mapUserDtoToJson;

@ExtendWith(MockitoExtension.class)
class KafkaMessageProcessorTests {

    @Mock
    private UserService userService;
    private final Gson gson = new Gson();
    private KafkaMessageProcessor kafkaMessageProcessor;

    @BeforeEach
    void setUp() {
        kafkaMessageProcessor = new KafkaMessageProcessor(gson, userService);
    }

    @Test
    void shouldProcessUserRegistrationMessage() {
        kafkaMessageProcessor.processUserRegistrationMessage(
                mapUserDtoToJson(UserDto.of("1", "username", "email", "123")));

        verify(userService).createUserFromDto(UserDto.of("1", "username", "email", "123"));
    }
}