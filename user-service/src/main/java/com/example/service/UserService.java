package com.example.service;

import com.example.dto.UserDto;
import com.example.dto.mq_dto.RegistrationDto;
import com.example.dto.request.UpdateUserRequest;
import com.example.entity.User;

import java.util.List;

public interface UserService {

    List<UserDto> getAllUserDtos();

    UserDto getUserDtoById(Long id);

    UserDto getUserDtoByEmail(String email);

    void updateUserByIdFromUpdateRequest(Long id, UpdateUserRequest updateUserRequest);

    void createUserFromRegistrationDto(RegistrationDto registrationDto);

    User findUserByIdInDatabase(Long id);

    User findUserByEmailInDatabase(String email);
}
