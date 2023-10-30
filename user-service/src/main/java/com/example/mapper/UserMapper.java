package com.example.mapper;

import com.example.dto.mq_dto.RegistrationDto;
import com.example.dto.mq_dto.UpdateDto;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.UserDto;
import com.example.entity.User;
import org.mapstruct.*;

import java.util.Collection;
import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserDto mapUserToDto(User user);

    UpdateDto mapUserToUpdateDto(User user);

    User mapRegistrationDtoToUser(RegistrationDto registrationDto);

    User updateUserFromUpdateRequest(@MappingTarget User userToUpdate, UpdateUserRequest updateRequest);

    List<UserDto> mapUsersToDtos(Collection<User> users);
}
