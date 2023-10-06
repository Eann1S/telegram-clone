package com.example.mapper;

import com.example.dto.mq_dto.RegistrationDto;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(InstancioExtension.class)
class UserMapperTests {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = Mappers.getMapper(UserMapper.class);
    }

    @ParameterizedTest
    @InstancioSource
    void shouldMapUserToDto(User user) {
        UserDto userDto = userMapper.mapUserToDto(user);

        assertThat(userDto)
                .extracting(UserDto::id, UserDto::email, UserDto::username)
                .containsExactly(user.getId(), user.getEmail(), user.getUsername());
    }

    @ParameterizedTest
    @InstancioSource
    void shouldMapRegistrationDtoToUser(RegistrationDto registrationDto) {
        User user = userMapper.mapRegistrationDtoToUser(registrationDto);

        assertThat(user)
                .extracting(User::getId, User::getEmail, User::getUsername)
                .containsExactly(registrationDto.id(), registrationDto.email(), registrationDto.username());
    }

    @ParameterizedTest
    @InstancioSource
    void shouldUpdateUserFromUpdateRequest(User user, UpdateUserRequest request) {
        User updatedUser = userMapper.updateUserFieldsFromUpdateRequest(user, request);

        assertThat(updatedUser)
                .extracting(User::getId, User::getEmail, User::getUsername)
                .containsExactly(user.getId(), request.email(), request.username());
    }

    @ParameterizedTest
    @InstancioSource
    void shouldMapUsersToDtos(User user1, User user2) {
        List<User> users = List.of(user1, user2);

        Collection<UserDto> userDtos = userMapper.mapUsersToDtos(users);

        assertThat(userDtos)
                .extracting(UserDto::id)
                .containsExactly(user1.getId(), user2.getId());
    }
}