package com.example.service;

import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final MessageGenerator messageGenerator;

    public Collection<UserDto> getAllUsers() {
        Collection<User> users = userRepository.findAll();
        return mapUsersToDtos(users);
    }

    public UserDto getUserById(String id) {
        User user = findUserByIdInDatabase(id);
        return userMapper.mapUserToDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = findUserByEmailInDatabase(email);
        return userMapper.mapUserToDto(user);
    }

    public UserDto updateUserByIdFromUpdateRequest(String id, UpdateUserRequest updateUserRequest) {
        User userToUpdate = findUserByIdInDatabase(id);
        User updatedUser = userMapper.updateUserFromUpdateRequest(userToUpdate, updateUserRequest);
        return userMapper.mapUserToDto(updatedUser);
    }

    User findUserByIdInDatabase(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return getUserFromOptionalOtherwiseThrowExceptionWithParameter(optionalUser, id);
    }

    User findUserByEmailInDatabase(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return getUserFromOptionalOtherwiseThrowExceptionWithParameter(optionalUser, email);
    }

    User getUserFromOptionalOtherwiseThrowExceptionWithParameter(Optional<User> optionalUser, Object param) {
        return optionalUser
                .orElseThrow(() -> new EntityNotFoundException(
                        messageGenerator.generateMessage("error.entity.not_found", param))
                );
    }

    Collection<UserDto> mapUsersToDtos(Collection<User> users) {
        return users.stream()
                .map(userMapper::mapUserToDto)
                .collect(Collectors.toList());
    }
}
