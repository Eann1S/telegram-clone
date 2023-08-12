package com.example.mapper;

import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static util.TestUserCreator.createUserWithAllFields;
import static util.TestUserCreator.createUserWithId;

class UserMapperTests {

    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Test
    void shouldMapUserToDto() {
        User user = createUserWithAllFields("1", "username", "email", "123");

        UserDto actualUserDto = userMapper.mapUserToDto(user);

        assertThat(actualUserDto)
                .extracting(UserDto::id, UserDto::username, UserDto::email, UserDto::phoneNumber)
                .containsExactly("1", "username", "email", "123");
    }

    @Test
    void shouldMapDtoToUser() {
        UserDto userDto = UserDto.of("1", "username", "email", "123");

        User actualUser = userMapper.mapDtoToUser(userDto);

        assertThat(actualUser)
                .extracting(User::getId, User::getUsername, User::getEmail, User::getPhoneNumber)
                .containsExactly("1", "username", "email", "123");
    }

    @Test
    void shouldUpdateUserFromUpdateRequest() {
        User userToUpdate = createUserWithAllFields("1", "username", "email", "123");

        User updatedUser = userMapper.updateUserFromUpdateRequest(userToUpdate,
                UpdateUserRequest.of("user", "gmail", "456"));

        assertThat(updatedUser)
                .extracting(User::getId, User::getUsername, User::getEmail, User::getPhoneNumber)
                .containsExactly("1", "user", "gmail", "456");
    }

    @Test
    void shouldMapCollectionOfUsersToDtos_whenCollectionIsNotEmpty() {
        Collection<User> users = List.of(
                createUserWithId("1"),
                createUserWithId("2")
        );

        Collection<UserDto> actualUserDtos = userMapper.mapUsersToDtos(users);

        assertThat(actualUserDtos)
                .extracting(UserDto::id)
                .containsOnly("1", "2");
    }
}