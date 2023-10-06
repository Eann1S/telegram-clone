package com.example.service;

import com.example.dto.mq_dto.RegistrationDto;
import com.example.dto.mq_dto.UpdateDto;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.messaging.UserMessagingService;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static com.example.message.ErrorMessage.ENTITY_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMessagingService userMessagingService;
    @Mock
    private UserMapper userMapper;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userMapper, userMessagingService);
    }

    @Nested
    class SuccessCases {
        @ParameterizedTest
        @InstancioSource
        void shouldReturnUserDtosForUsers_whenUsersExist(List<User> users, List<UserDto> userDtos) {
            when(userRepository.findAll())
                    .thenReturn(users);
            when(userMapper.mapUsersToDtos(users))
                    .thenReturn(userDtos);

            Collection<UserDto> actualUserDtos = userService.getAllUsers();

            assertThat(actualUserDtos).containsExactlyElementsOf(userDtos);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldReturnUserDtoById_whenUserExists(User user, UserDto userDto) {
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));
            when(userMapper.mapUserToDto(user))
                    .thenReturn(userDto);

            UserDto actualUserDto = userService.getUserById(user.getId());

            assertThat(actualUserDto).isEqualTo(userDto);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldReturnUserDtoByEmail_whenUserExists(User user, UserDto userDto) {
            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));
            when(userMapper.mapUserToDto(user))
                    .thenReturn(userDto);

            UserDto actualUserDto = userService.getUserByEmail(user.getEmail());

            assertThat(actualUserDto).isEqualTo(userDto);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldUpdateUserById(User user, User updatedUser, UpdateDto updateDto, UpdateUserRequest request) {
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));
            when(userMapper.updateUserFieldsFromUpdateRequest(user, request))
                    .thenReturn(updatedUser);
            when(userMapper.mapUserToUpdateDto(updatedUser))
                    .thenReturn(updateDto);

            userService.updateUserByIdFromUpdateRequest(user.getId(), request);

            verify(userMessagingService).send(updateDto);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldFindUserByIdInDatabase_whenGivenValidId(User user) {
            when(userRepository.findById(user.getId()))
                    .thenReturn(Optional.of(user));

            User actualUser = userService.findUserByIdInDatabase(user.getId());

            assertThat(actualUser).isEqualTo(user);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldFindUserByEmailInDatabase_whenGivenValidEmail(User user) {
            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));

            User actualUser = userService.findUserByEmailInDatabase(user.getEmail());

            assertThat(actualUser).isEqualTo(user);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldCreateUserFromRegistrationDto(User user, RegistrationDto registrationDto) {
            when(userMapper.mapRegistrationDtoToUser(registrationDto))
                    .thenReturn(user);

            userService.createUserFrom(registrationDto);

            verify(userRepository).save(user);
        }
    }

    @Nested
    class FailureCases {

        @ParameterizedTest
        @InstancioSource
        void shouldThrowException_whenUserWithGivenIdDoesNotExist(Long id) {
            assertThatThrownBy(() -> userService.getUserById(id))
                    .hasMessage(ENTITY_NOT_FOUND.formatWith(id));
        }

        @ParameterizedTest
        @InstancioSource
        void shouldThrowException_whenUserWithGivenEmailDoesNotExist(String email) {
            assertThatThrownBy(() -> userService.getUserByEmail(email))
                    .hasMessage(ENTITY_NOT_FOUND.formatWith(email));
        }
    }
}