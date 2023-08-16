package integration_tests.controller;

import com.example.UserServiceApplication;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.ErrorResponse;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.MessageGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import integration_tests.annotation.DisableKafkaAutoConfiguration;
import integration_tests.starter.DatabaseStarter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

import static constant.UrlConstant.*;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static util.TestUserCreator.*;

@SpringBootTest(classes = UserServiceApplication.class)
@ActiveProfiles("test")
@MockBean({KafkaTemplate.class, KafkaAdmin.class})
@DisableKafkaAutoConfiguration
@AutoConfigureMockMvc
@Slf4j
@SuppressWarnings("SameParameterValue")
public class UserControllerIntegrationTests implements DatabaseStarter {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    class SuccessCases {
        @Test
        void shouldReturnListOfUsers_whenUsersExist() throws Exception {
            saveToDatabase(
                    createUserWithId("1"),
                    createUserWithId("2")
            );

            String jsonResponse = requestUsers();

            assertThat(mapJsonResponseToUserDtos(jsonResponse))
                    .extracting(UserDto::id)
                    .containsOnly("1", "2");
        }

        @Test
        void shouldReturnEmptyListOfUsers_whenUsersDontExist() throws Exception {
            String jsonResponse = requestUsers();

            assertThat(mapJsonResponseToUserDtos(jsonResponse)).isEmpty();
        }

        @Test
        void shouldReturnUser_whenGivenValidId() throws Exception {
            saveToDatabase(
                    createUserWithId("1")
            );

            String jsonResponse = requestUserById("1");

            assertThat(mapJsonResponseToUserDto(jsonResponse))
                    .extracting(UserDto::id, as(STRING))
                    .isEqualTo("1");
        }

        @Test
        void shouldReturnUser_whenGivenValidEmail() throws Exception {
            saveToDatabase(
                    createUserWithEmail("email")
            );

            String jsonResponse = requestUserByEmail("email");

            assertThat(mapJsonResponseToUserDto(jsonResponse))
                    .extracting(UserDto::email, as(STRING))
                    .isEqualTo("email");
        }

        @Test
        void shouldReturnUser_whenGivenValidPhoneNumber() throws Exception {
            saveToDatabase(
                    createUserWithPhoneNumber("12345678")
            );

            String jsonResponse = requestUserByPhoneNumber("12345678");

            assertThat(mapJsonResponseToUserDto(jsonResponse))
                    .extracting(UserDto::phoneNumber, as(STRING))
                    .isEqualTo("12345678");
        }

        @Test
        void shouldUpdateUser_whenGivenValidId() throws Exception {
            saveToDatabase(
                    createUserWithAllFields("1", "username", "email", "12345")
            );

            UpdateUserRequest updateUserRequest = UpdateUserRequest.of("new username", "new email", "67890");
            String jsonResponse = updateUserById("1", updateUserRequest);

            assertThat(mapJsonResponseToUserDto(jsonResponse))
                    .isEqualTo(UserDto.of("1", "new username", "new email", "67890"));
        }
    }

    @Nested
    class FailureCases {
        @Test
        void shouldThrowException_whenGivenInvalidId() throws Exception {
            saveToDatabase(
                    createUserWithId("1")
            );

            String jsonResponse = requestUserById("2");

            assertThat(mapJsonResponseToErrorResponse(jsonResponse))
                    .extracting(ErrorResponse::statusCode, ErrorResponse::errorMessage)
                    .containsExactly(NOT_FOUND.value(), MessageGenerator.generateEntityNotFoundMessage("2"));
        }

        @Test
        void shouldThrowException_whenGivenInvalidEmail() throws Exception {
            saveToDatabase(
                    createUserWithEmail("email")
            );

            String jsonResponse = requestUserByEmail("invalid email");

            assertThat(mapJsonResponseToErrorResponse(jsonResponse))
                    .extracting(ErrorResponse::statusCode, ErrorResponse::errorMessage)
                    .containsExactly(NOT_FOUND.value(), MessageGenerator.generateEntityNotFoundMessage("invalid email"));
        }

        @Test
        void shouldThrowException_whenGivenInvalidPhoneNumber() throws Exception {
            saveToDatabase(
                    createUserWithPhoneNumber("12345678")
            );

            String jsonResponse = requestUserByPhoneNumber("87654321");

            assertThat(mapJsonResponseToErrorResponse(jsonResponse))
                    .extracting(ErrorResponse::statusCode, ErrorResponse::errorMessage)
                    .containsExactly(NOT_FOUND.value(), MessageGenerator.generateEntityNotFoundMessage("87654321"));
        }
    }

    private String requestUsers() throws Exception {
        ResultActions resultActions = mockMvc.perform(get(USERS_URL));
        return getContentAsStringFrom(resultActions);
    }

    private String requestUserById(String id) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(USER_URL_WITH_VARIABLE, id));
        return getContentAsStringFrom(resultActions);
    }

    private String requestUserByEmail(String email) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(USER_URL).param("email", email));
        return getContentAsStringFrom(resultActions);
    }

    private String requestUserByPhoneNumber(String phoneNumber) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(USER_URL).param("phoneNumber", phoneNumber));
        return getContentAsStringFrom(resultActions);
    }

    private String updateUserById(String id, UpdateUserRequest updateUserRequest) throws Exception {
        ResultActions resultActions = mockMvc.perform(
                put(USER_URL_WITH_VARIABLE, id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateUserRequest))
        );
        return getContentAsStringFrom(resultActions);
    }

    private String getContentAsStringFrom(ResultActions resultActions) throws UnsupportedEncodingException {
        return resultActions.andReturn().getResponse().getContentAsString(StandardCharsets.UTF_8);
    }

    private ErrorResponse mapJsonResponseToErrorResponse(String jsonResponse) throws JsonProcessingException {
        return objectMapper.readValue(jsonResponse, ErrorResponse.class);
    }

    private UserDto mapJsonResponseToUserDto(String jsonResponse) throws JsonProcessingException {
        return objectMapper.readValue(jsonResponse, UserDto.class);
    }

    private Collection<UserDto> mapJsonResponseToUserDtos(String jsonResponse) throws JsonProcessingException {
        return objectMapper.readValue(jsonResponse, getTypeReferenceForListOfUserDtos());
    }

    @SuppressWarnings("Convert2Diamond")
    private TypeReference<Collection<UserDto>> getTypeReferenceForListOfUserDtos() {
        return new TypeReference<Collection<UserDto>>() {
        };
    }

    private void saveToDatabase(User... users) {
        userRepository.saveAll(Arrays.asList(users));
        log.info("new users are inserted into a database, now there are {} in the database", userRepository.findAll());
    }
}
