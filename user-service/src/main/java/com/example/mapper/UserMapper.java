package com.example.mapper;

import com.example.dto.mq_dto.RegistrationDto;
import com.example.dto.mq_dto.UpdateDto;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.UserDto;
import com.example.entity.User;
import org.mapstruct.*;

import java.util.Collection;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    UserDto mapUserToDto(User user);

    UpdateDto mapUserToUpdateDto(User user);

    User mapRegistrationDtoToUser(RegistrationDto registrationDto);

    User updateUserFieldsFromUpdateRequest(@MappingTarget User userToUpdate, UpdateUserRequest updateRequest);

    Collection<UserDto> mapUsersToDtos(Collection<User> users);
}
