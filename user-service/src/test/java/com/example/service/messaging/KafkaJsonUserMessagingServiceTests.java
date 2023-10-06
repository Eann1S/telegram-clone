package com.example.service.messaging;

import com.example.config.kafka.KafkaTopicConfig;
import com.example.dto.mq_dto.UpdateDto;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static com.example.json.JsonConverter.toJson;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class KafkaJsonUserMessagingServiceTests {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;
    @Mock
    private KafkaTopicConfig topicConfig;
    private KafkaJsonUserMessagingService userMessagingService;

    @BeforeEach
    void setUp() {
        userMessagingService = new KafkaJsonUserMessagingService(kafkaTemplate, topicConfig);
    }

    @ParameterizedTest
    @InstancioSource
    void shouldSendUpdateDto(UpdateDto updateDto, String userUpdateTopic) {
        when(topicConfig.getUserUpdateTopic())
                .thenReturn(userUpdateTopic);

        userMessagingService.send(updateDto);

        verify(kafkaTemplate).send(userUpdateTopic, toJson(updateDto));
    }
}