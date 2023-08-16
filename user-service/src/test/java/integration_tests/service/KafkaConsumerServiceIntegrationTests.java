package integration_tests.service;

import com.example.UserServiceApplication;
import com.example.dto.kafka_message.KafkaMessage;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import com.example.repository.UserRepository;
import integration_tests.annotation.DisableDatabaseAutoConfiguration;
import integration_tests.starter.KafkaStarter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

import static com.example.json.JsonMapper.toJson;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

@SpringBootTest(classes = UserServiceApplication.class)
@ActiveProfiles("test")
@DisableDatabaseAutoConfiguration
public class KafkaConsumerServiceIntegrationTests implements KafkaStarter {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Value("${kafka.topics.user-registration}")
    private String userRegistrationTopic;


    @Test
    void shouldCreateUserFromRegistrationMessage_whenMessageIsSent() {
        sendToKafkaTopic(userRegistrationTopic,
                toJson(KafkaMessage.of(UserDto.of("1", "username", "email", "123"))));

        verify(userRepository, timeout(3000)).save(new User("1", "username", "email", "123"));
    }

    private <T> void sendToKafkaTopic(String topic, T message) {
        kafkaTemplate.send(topic, message);
    }
}
