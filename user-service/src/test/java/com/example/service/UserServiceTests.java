package com.example.service;

import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.TestUserCreator.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private KafkaProducerService kafkaProducerService;
    @Spy
    @SuppressWarnings("unused")
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @InjectMocks
    private UserService userService;

    @Nested
    class SuccessCases {
        @Test
        void shouldReturnUserDtosForAllUsers_whenUsersExist() {
            when(userRepository.findAll()).thenReturn(List.of(
                    createUserWithId("1"),
                    createUserWithId("2")
            ));

            Collection<UserDto> actualUserDtos = userService.getAllUsers();

            assertThat(actualUserDtos)
                    .extracting(UserDto::id)
                    .containsOnly("1", "2");
        }

        @Test
        void shouldReturnUserDtoById_whenUserExists() {
            when(userRepository.findById("1"))
                    .thenReturn(Optional.of(createUserWithId("1")));

            UserDto actualUserDto = userService.getUserById("1");

            assertThat(actualUserDto)
                    .extracting(UserDto::id)
                    .isEqualTo("1");
        }

        @Test
        void shouldReturnUserDtoByEmail_whenUserExists() {
            when(userRepository.findByEmail("email"))
                    .thenReturn(Optional.of(createUserWithEmail("email")));

            UserDto actualUserDto = userService.getUserByEmail("email");

            assertThat(actualUserDto)
                    .extracting(UserDto::email)
                    .isEqualTo("email");
        }

        @Test
        void shouldReturnUserDtoByPhoneNumber_whenUserExists() {
            when(userRepository.findByPhoneNumber("123"))
                    .thenReturn(Optional.of(createUserWithPhoneNumber("123")));

            UserDto actualUserDto = userService.getUserByPhoneNumber("123");

            assertThat(actualUserDto)
                    .extracting(UserDto::phoneNumber)
                    .isEqualTo("123");
        }

        @Test
        void shouldUpdateUserById() {
            when(userRepository.findById("1"))
                    .thenReturn(Optional.of(createUserWithId("1")));

            UserDto actualUserDto = userService.updateUserByIdFromUpdateRequest("1",
                    UpdateUserRequest.of("new username", "new email", "890"));

            verify(kafkaProducerService).sendUserUpdateMessage(actualUserDto);
            assertThat(actualUserDto)
                    .extracting(UserDto::id, UserDto::username, UserDto::email, UserDto::phoneNumber)
                    .containsExactly("1", "new username", "new email", "890");
        }

        @ParameterizedTest
        @MethodSource("util.TestParameterFactories#userFactory")
        void shouldFindUserByIdInDatabase_whenGivenValidId(User user) {
            when(userRepository.findById("1"))
                    .thenReturn(Optional.of(user));

            User actualUser = userService.findUserByIdInDatabase("1");

            assertThat(actualUser).isEqualTo(user);
        }

        @ParameterizedTest
        @MethodSource("util.TestParameterFactories#userFactory")
        void shouldFindUserByEmailInDatabase_whenGivenValidEmail(User user) {
            when(userRepository.findByEmail("email"))
                    .thenReturn(Optional.of(user));

            User actualUser = userService.findUserByEmailInDatabase("email");

            assertThat(actualUser).isEqualTo(user);
        }

        @ParameterizedTest
        @MethodSource("util.TestParameterFactories#userFactory")
        void shouldFindUserByPhoneNumberInDatabase_whenGivenValidPhoneNumber(User user) {
            when(userRepository.findByPhoneNumber("123"))
                    .thenReturn(Optional.of(user));

            User actualUser = userService.findUserByPhoneNumberInDatabase("123");

            assertThat(actualUser).isEqualTo(user);
        }

        @Test
        void shouldCreateUserFromDto() {
            userService.createUserFrom(UserDto.of("1", "username", "email", "123"));

            verify(userRepository).save(new User("1", "username", "email", "123"));
        }
    }

    @Nested
    class FailureCases {

        @Test
        void shouldThrowException_whenUserWithGivenIdDoesNotExist() {
            assertThatThrownBy(() -> userService.getUserById("1"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(MessageGenerator.generateEntityNotFoundMessage("1"));
        }

        @Test
        void shouldThrowException_whenUserWithGivenEmailDoesNotExist() {
            assertThatThrownBy(() -> userService.getUserByEmail("email"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(MessageGenerator.generateEntityNotFoundMessage("email"));
        }

        @Test
        void shouldThrowException_whenUserWithGivenPhoneNumberDoesNotExist() {
            assertThatThrownBy(() -> userService.getUserByPhoneNumber("123"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(MessageGenerator.generateEntityNotFoundMessage("123"));
        }
    }
}