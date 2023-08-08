package com.example.mapper;

import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserDto mapUserToDto(User user);

    @Mapping(target = "friends", ignore = true)
    User updateUserFromUpdateRequest(@MappingTarget User userToUpdate, UpdateUserRequest updateRequest);
}
