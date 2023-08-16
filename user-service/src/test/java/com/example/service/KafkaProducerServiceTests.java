package com.example.service;

import com.example.dto.kafka_message.KafkaMessage;
import com.example.dto.response.UserDto;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static com.example.json.JsonMapper.toJson;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTests {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @InjectMocks
    private KafkaProducerService kafkaProducerService;

    @ParameterizedTest
    @MethodSource("util.TestParameterFactories#userDtoFactory")
    void shouldSendUserUpdateMessage(UserDto userDto) {
        kafkaProducerService.sendUserUpdateMessage(userDto);

        verify(kafkaTemplate).send(any(), eq(toJson(KafkaMessage.of(userDto))));
    }
}