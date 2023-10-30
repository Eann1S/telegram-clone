package integration_tests.service;

import com.example.UserServiceApplication;
import com.example.dto.mq_dto.UpdateDto;
import com.example.service.impl.KafkaJsonUserMessagingService;
import org.apache.commons.lang.StringUtils;
import org.awaitility.Awaitility;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import test_util.annotation.DisableDatabaseAutoConfiguration;
import test_util.starter.ConfigServerStarter;
import test_util.starter.KafkaStarter;

import java.util.concurrent.TimeUnit;

import static com.example.json.JsonConverter.fromJson;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext
@SpringBootTest(classes = {UserServiceApplication.class, KafkaJsonUserMessagingServiceIntegrationTests.TestKafkaListener.class})
@ActiveProfiles("test")
@ExtendWith(InstancioExtension.class)
@DisableDatabaseAutoConfiguration
public class KafkaJsonUserMessagingServiceIntegrationTests implements ConfigServerStarter, KafkaStarter {

    @Autowired
    private KafkaJsonUserMessagingService userMessagingService;
    @Autowired
    private TestKafkaListener testKafkaListener;

    @ParameterizedTest
    @InstancioSource
    void shouldSendUpdateDto(UpdateDto updateDto) {
        userMessagingService.send(updateDto);
        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> testKafkaListener.isMessageReceived());

        UpdateDto actualUpdateDto = fromJson(testKafkaListener.messagePayload, UpdateDto.class);
        assertThat(actualUpdateDto).isEqualTo(updateDto);
    }

    @TestComponent
    static class TestKafkaListener {
        private String messagePayload;

        @KafkaListener(topics = "#{kafkaTopicConfig.getUserUpdateTopic()}")
        void receiveUserUpdateMessage(String message) {
            messagePayload = message;
        }

        private boolean isMessageReceived() {
            return StringUtils.isNotBlank(messagePayload);
        }
    }
}

