package com.example.service;

import com.example.dto.kafka_message.KafkaMessage;
import com.example.dto.response.UserDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.json.JsonMapper.toJson;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTests {

    @Mock
    private UserService userService;
    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @ParameterizedTest
    @MethodSource("util.TestParameterFactories#userDtoFactory")
    void shouldCreateUserFromRegistrationMessage(UserDto userDto) {
        kafkaConsumerService.createUserFromRegistrationMessage(toJson(KafkaMessage.of(userDto)));

        verify(userService).createUserFrom(userDto);
    }
}