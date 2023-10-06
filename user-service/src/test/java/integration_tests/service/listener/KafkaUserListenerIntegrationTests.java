package integration_tests.service.listener;

import com.example.UserServiceApplication;
import com.example.config.kafka.KafkaTopicConfig;
import com.example.dto.mq_dto.RegistrationDto;
import com.example.service.UserService;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import test_util.annotation.DisableDatabaseAutoConfiguration;
import test_util.starter.ConfigServerStarter;
import test_util.starter.KafkaStarter;

import static com.example.json.JsonConverter.toJson;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = UserServiceApplication.class)
@ActiveProfiles("test")
@ExtendWith(InstancioExtension.class)
@DisableDatabaseAutoConfiguration
public class KafkaUserListenerIntegrationTests implements ConfigServerStarter, KafkaStarter {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @SpyBean
    private UserService userService;
    @Autowired
    private KafkaTopicConfig topicConfig;

    @ParameterizedTest
    @InstancioSource
    void shouldCreateUserFromRegistrationDto_whenDtoIsSent(RegistrationDto registrationDto) {
        sendMessage(topicConfig.getRegistrationTopic(), toJson(registrationDto));

        verify(userService, timeout(3000)).createUserFrom(registrationDto);
    }

    private <T> void sendMessage(String topic, T message) {
        kafkaTemplate.send(topic, message);
    }
}
