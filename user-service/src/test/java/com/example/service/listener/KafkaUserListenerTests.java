package com.example.service.listener;

import com.example.dto.mq_dto.RegistrationDto;
import com.example.service.UserService;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.json.JsonConverter.toJson;
import static org.mockito.Mockito.verify;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class KafkaUserListenerTests {

    @Mock
    private UserService userService;
    private KafkaUserListener kafkaUserListener;

    @BeforeEach
    void setUp() {
        kafkaUserListener = new KafkaUserListener(userService);
    }

    @ParameterizedTest
    @InstancioSource
    void shouldCreateUserFromRegistrationDtoMessage(RegistrationDto registrationDto) {
        kafkaUserListener.createUserFromRegistrationDtoMessage(toJson(registrationDto));

        verify(userService).createUserFrom(registrationDto);
    }
}