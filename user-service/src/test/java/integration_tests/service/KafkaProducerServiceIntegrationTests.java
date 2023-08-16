package integration_tests.service;

import com.example.UserServiceApplication;
import com.example.dto.kafka_message.KafkaMessage;
import com.example.dto.response.UserDto;
import com.example.repository.UserRepository;
import com.example.service.KafkaProducerService;
import com.google.gson.reflect.TypeToken;
import integration_tests.annotation.DisableDatabaseAutoConfiguration;
import integration_tests.starter.KafkaStarter;
import org.apache.commons.lang.StringUtils;
import org.awaitility.Awaitility;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.TimeUnit;

import static com.example.json.JsonMapper.fromJson;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {UserServiceApplication.class, KafkaProducerServiceIntegrationTests.TestKafkaConsumer.class})
@ActiveProfiles("test")
@MockBean(UserRepository.class)
@DisableDatabaseAutoConfiguration
public class KafkaProducerServiceIntegrationTests implements KafkaStarter {

    @Autowired
    private KafkaProducerService kafkaProducerService;
    @Autowired
    private TestKafkaConsumer testKafkaConsumer;

    @ParameterizedTest
    @MethodSource("util.TestParameterFactories#userDtoFactory")
    void shouldSendUserUpdateMessage(UserDto userDto) {
        kafkaProducerService.sendUserUpdateMessage(userDto);
        Awaitility.await().atMost(5, TimeUnit.SECONDS).until(() -> testKafkaConsumer.isMessageReceived());

        KafkaMessage<UserDto> kafkaMessage = fromJson(testKafkaConsumer.messagePayload, new TypeToken<>(){});
        assertThat(kafkaMessage)
                .extracting(KafkaMessage::content)
                .isEqualTo(userDto);
    }

    @TestComponent
    static class TestKafkaConsumer {
        private String messagePayload;

        @KafkaListener(topics = "${kafka.topics.user-update}")
        void receiveUserUpdateMessage(String message) {
            messagePayload = message;
        }

        private boolean isMessageReceived() {
            return StringUtils.isNotBlank(messagePayload);
        }
    }
}

