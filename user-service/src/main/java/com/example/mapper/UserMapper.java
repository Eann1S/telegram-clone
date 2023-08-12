package com.example.mapper;

import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import org.mapstruct.*;

import java.util.Collection;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserDto mapUserToDto(User user);

    User mapDtoToUser(UserDto userDto);

    User updateUserFromUpdateRequest(@MappingTarget User userToUpdate, UpdateUserRequest updateRequest);

    Collection<UserDto> mapUsersToDtos(Collection<User> users);
}
