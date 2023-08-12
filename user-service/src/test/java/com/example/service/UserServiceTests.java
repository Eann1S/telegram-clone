package com.example.service;

import com.example.dto.response.UserDto;
import com.example.entity.User;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.InstanceOfAssertFactories.STRING;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static util.TestUserCreator.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Spy
    @SuppressWarnings("unused")
    private UserMapper userMapper = Mappers.getMapper(UserMapper.class);
    @InjectMocks
    private UserService userService;

    @Nested
    class SuccessCases {
        @Test
        void shouldFindUserByIdInDatabase_whenGivenValidId() {
            when(userRepository.findById("1"))
                    .thenReturn(Optional.of(createUserWithId("1")));

            User actualUser = userService.findUserByIdInDatabase("1");

            assertThat(actualUser)
                    .extracting(User::getId, as(STRING))
                    .isEqualTo("1");
        }

        @Test
        void shouldFindUserByEmailInDatabase_whenGivenValidEmail() {
            when(userRepository.findByEmail("email"))
                    .thenReturn(Optional.of(createUserWithEmail("email")));

            User actualUser = userService.findUserByEmailInDatabase("email");

            assertThat(actualUser)
                    .extracting(User::getEmail, as(STRING))
                    .isEqualTo("email");
        }

        @Test
        void shouldFindUserByPhoneNumberInDatabase_whenGivenValidPhoneNumber() {
            when(userRepository.findByPhoneNumber("123"))
                    .thenReturn(Optional.of(createUserWithPhoneNumber("123")));

            User actualUser = userService.findUserByPhoneNumberInDatabase("123");

            assertThat(actualUser)
                    .extracting(User::getPhoneNumber, as(STRING))
                    .isEqualTo("123");
        }

        @Test
        void shouldCreateUserFromDto() {
            userService.createUserFromDto(UserDto.of("1", "username", "email", "123"));

            verify(userRepository).save(new User("1", "username", "email", "123"));
        }
    }

    @Nested
    class FailureCases {

        @Test
        void shouldThrowException_whenUserDoesNotExist() {
            assertThatThrownBy(() -> userService.getUserById("1"))
                    .isInstanceOf(EntityNotFoundException.class)
                    .hasMessage(MessageGenerator.generateEntityNotFoundMessage("1"));
        }
    }
}