package com.example.controller;

import com.example.DatabaseStarter;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.ErrorResponse;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.instancio.Instancio;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

import static com.example.constant.UrlConstant.*;
import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.instancio.Select.field;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Slf4j
@SuppressWarnings("SameParameterValue")
public class UserControllerTests implements DatabaseStarter {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Value("${error.entity.not_found}")
    private String NOT_FOUND_MESSAGE;

    @Nested
    class SuccessCases {
        @Test
        public void shouldReturnListOfUsers_whenUsersExist() throws Exception {
            saveToDatabase(
                    createUserWithAllFields("1", "username1", "email1", "123"),
                    createUserWithAllFields("2", "username2", "email2", "456")
            );

            String jsonResponse = requestUsers();

            assertThat(mapJsonResponseToUserDtos(jsonResponse))
                    .containsOnly(
                            UserDto.of("1", "username1", "email1", "123"),
                            UserDto.of("2", "username2", "email2", "456")
                    );
        }

        @Test
        public void shouldReturnEmptyListOfUsers_whenUsersDontExist() throws Exception {
            String jsonResponse = requestUsers();

            assertThat(mapJsonResponseToUserDtos(jsonResponse)).isEmpty();
        }

        @Test
        public void shouldReturnUser_whenGivenValidId() throws Exception {
            saveToDatabase(
                    createUserWithId("1")
            );

            String jsonResponse = requestUserById("1");

            assertThat(mapJsonResponseToUserDto(jsonResponse))
                    .extracting(UserDto::id, as(STRING))
                    .isEqualTo("1");
        }

        @Test
        public void shouldReturnCorrectUser_whenGivenValidEmail() throws Exception {
            saveToDatabase(
                    createUserWithEmail("email")
            );

            String jsonResponse = requestUserByEmail("email");

            assertThat(mapJsonResponseToUserDto(jsonResponse))
                    .extracting(UserDto::email, as(STRING))
                    .isEqualTo("email");
        }

        @Test
        public void shouldUpdateUser_whenGivenValidId() throws Exception {
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
        public void shouldThrowException_whenGivenInvalidId() throws Exception {
            saveToDatabase(
                    createUserWithId("1")
            );

            String jsonResponse = requestUserById("2");

            assertThat(mapJsonResponseToErrorResponse(jsonResponse))
                    .extracting(ErrorResponse::statusCode, ErrorResponse::errorMessage)
                    .containsExactly(NOT_FOUND.value(), NOT_FOUND_MESSAGE.formatted("2"));
        }

        @Test
        public void shouldThrowException_whenGivenInvalidEmail() throws Exception {
            saveToDatabase(
                    createUserWithEmail("email")
            );

            String jsonResponse = requestUserByEmail("invalid email");

            assertThat(mapJsonResponseToErrorResponse(jsonResponse))
                    .extracting(ErrorResponse::statusCode, ErrorResponse::errorMessage)
                    .containsExactly(NOT_FOUND.value(), NOT_FOUND_MESSAGE.formatted("invalid email"));
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

    private User createUserWithAllFields(String id, String username, String email, String phoneNumber) {
        return Instancio.of(User.class)
                .set(field(User::getId), id)
                .set(field(User::getUsername), username)
                .set(field(User::getEmail), email)
                .set(field(User::getPhoneNumber), phoneNumber)
                .create();
    }

    private User createUserWithId(String id) {
        return Instancio.of(User.class)
                .set(field(User::getId), id)
                .create();
    }

    private User createUserWithEmail(String email) {
        return Instancio.of(User.class)
                .set(field(User::getEmail), email)
                .create();
    }
}
