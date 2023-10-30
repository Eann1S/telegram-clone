package integration_tests.controller;

import com.example.UserServiceApplication;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.UserDto;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import com.google.gson.reflect.TypeToken;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import test_util.starter.AllServicesStarter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;

import static com.example.json.JsonConverter.fromJson;
import static com.example.json.JsonConverter.toJson;
import static com.example.message.ErrorMessage.USER_NOT_FOUND;
import static com.example.message.InfoMessage.USER_UPDATED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static test_util.TestControllerUtil.getResponseContentWithExpectedStatus;
import static test_util.constant.UrlConstant.*;

@DirtiesContext
@SpringBootTest(classes = UserServiceApplication.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
@ExtendWith(InstancioExtension.class)
public class UserControllerIntegrationTests implements AllServicesStarter {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageSource messageSource;

    @Nested
    class SuccessCases {

        @ParameterizedTest
        @InstancioSource
        void shouldReturnListOfUsers_whenUsersExist(User user1, User user2) throws Exception {
            saveToDatabase(user1, user2);

            String jsonResponse = requestUsersAndExpectStatus(OK);

            Collection<UserDto> actualUsers = fromJson(jsonResponse, getTypeTokenForListWithUserDtos());
            assertThat(actualUsers)
                    .extracting(UserDto::id)
                    .containsOnly(user1.getId(), user2.getId());
        }

        @ParameterizedTest
        @InstancioSource
        void shouldReturnEmptyListOfUsers_whenUsersDontExist() throws Exception {
            String jsonResponse = requestUsersAndExpectStatus(OK);

            Collection<UserDto> actualUsers = fromJson(jsonResponse, getTypeTokenForListWithUserDtos());
            assertThat(actualUsers).isEmpty();
        }

        @ParameterizedTest
        @InstancioSource
        void shouldReturnUser_whenGivenValidId(User user) throws Exception {
            saveToDatabase(user);

            String jsonResponse = requestUserByIdAndExpectStatus(user.getId(), OK);

            UserDto actualUser = fromJson(jsonResponse, UserDto.class);
            assertThat(actualUser.id()).isEqualTo(user.getId());
        }

        @ParameterizedTest
        @InstancioSource
        void shouldReturnUser_whenGivenValidEmail(User user) throws Exception {
            saveToDatabase(user);

            String jsonResponse = requestUserByEmailAndExpectStatus(user.getEmail(), OK);

            UserDto actualUser = fromJson(jsonResponse, UserDto.class);
            assertThat(actualUser.email()).isEqualTo(user.getEmail());
        }

        @ParameterizedTest
        @InstancioSource
        void shouldUpdateUser_whenGivenValidId(User user) throws Exception {
            saveToDatabase(user);
            UpdateUserRequest request = UpdateUserRequest.of("email@email.com", "username");

            String jsonResponse = updateUserByIdAndExpectStatus(user.getId(), request, OK);

            assertThat(jsonResponse).contains(USER_UPDATED.getMessage());
            UserDto updatedUser = userService.getUserDtoById(user.getId());
            assertThat(updatedUser)
                    .extracting(UserDto::id, UserDto::email, UserDto::username)
                    .containsExactly(user.getId(), request.email(), request.username());
        }
    }

    @Nested
    class FailureCases {
        @ParameterizedTest
        @InstancioSource
        void shouldThrowException_whenGivenInvalidId(User user, Long invalidId) throws Exception {
            saveToDatabase(user);

            String jsonResponse = requestUserByIdAndExpectStatus(invalidId, NOT_FOUND);

            String errorMessage = USER_NOT_FOUND.formatWith(invalidId);
            assertThat(jsonResponse).contains(errorMessage);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldThrowException_whenGivenInvalidEmail(User user, String invalidEmail) throws Exception {
            saveToDatabase(user);

            String jsonResponse = requestUserByEmailAndExpectStatus(invalidEmail, NOT_FOUND);

            String errorMessage = USER_NOT_FOUND.formatWith(invalidEmail);
            assertThat(jsonResponse).contains(errorMessage);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldNotUpdateUser_whenGivenInvalidEmail(User user, UpdateUserRequest request) throws Exception {
            saveToDatabase(user);

            String jsonResponse = updateUserByIdAndExpectStatus(user.getId(), request, BAD_REQUEST);

            String errorMessage = getErrorMessageByCode("email.valid");
            assertThat(jsonResponse).contains(errorMessage);
        }
    }

    private String requestUsersAndExpectStatus(HttpStatus status) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(USERS_URL));
        return getResponseContentWithExpectedStatus(resultActions, status);
    }

    private String requestUserByIdAndExpectStatus(Long id, HttpStatus status) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(USER_URL_WITH_VARIABLE, id));
        return getResponseContentWithExpectedStatus(resultActions, status);
    }

    private String requestUserByEmailAndExpectStatus(String email, HttpStatus status) throws Exception {
        ResultActions resultActions = mockMvc.perform(get(USER_URL)
                .param("email", email));
        return getResponseContentWithExpectedStatus(resultActions, status);
    }

    private String updateUserByIdAndExpectStatus(Long id, UpdateUserRequest updateUserRequest, HttpStatus status) throws Exception {
        ResultActions resultActions = mockMvc.perform(put(UPDATE_USER_URL, id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(updateUserRequest)));
        return getResponseContentWithExpectedStatus(resultActions, status);
    }
    private TypeToken<Collection<UserDto>> getTypeTokenForListWithUserDtos() {
        return new TypeToken<>() {};
    }

    private void saveToDatabase(User... users) {
        userRepository.saveAll(Arrays.asList(users));
    }

    private String getErrorMessageByCode(String code, Object... args) {
        return messageSource.getMessage(code, args, Locale.getDefault());
    }
}
