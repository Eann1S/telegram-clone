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

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final KafkaProducerService kafkaProducerService;

    public Collection<UserDto> getAllUsers() {
        Collection<User> users = userRepository.findAll();
        return userMapper.mapUsersToDtos(users);
    }

    public UserDto getUserById(String id) {
        User user = findUserByIdInDatabase(id);
        return userMapper.mapUserToDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = findUserByEmailInDatabase(email);
        return userMapper.mapUserToDto(user);
    }

    public UserDto getUserByPhoneNumber(String phoneNumber) {
        User user = findUserByPhoneNumberInDatabase(phoneNumber);
        return userMapper.mapUserToDto(user);
    }

    public UserDto updateUserByIdFromUpdateRequest(String id, UpdateUserRequest updateUserRequest) {
        User userToUpdate = findUserByIdInDatabase(id);
        User updatedUser = userMapper.updateUserFromUpdateRequest(userToUpdate, updateUserRequest);
        UserDto userDto = userMapper.mapUserToDto(updatedUser);
        kafkaProducerService.sendUserUpdateMessage(userDto);
        return userDto;
    }

    User findUserByIdInDatabase(String id) {
        Optional<User> optionalUser = userRepository.findById(id);
        return getUserFromOptionalOtherwiseThrowExceptionWithParameter(optionalUser, id);
    }

    User findUserByEmailInDatabase(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        return getUserFromOptionalOtherwiseThrowExceptionWithParameter(optionalUser, email);
    }

    User findUserByPhoneNumberInDatabase(String phoneNumber) {
        Optional<User> optionalUser = userRepository.findByPhoneNumber(phoneNumber);
        return getUserFromOptionalOtherwiseThrowExceptionWithParameter(optionalUser, phoneNumber);
    }

    void createUserFrom(UserDto userDto) {
        User user = userMapper.mapDtoToUser(userDto);
        userRepository.save(user);
    }

    private User getUserFromOptionalOtherwiseThrowExceptionWithParameter(Optional<User> optionalUser, Object parameter) {
        return optionalUser.orElseThrow(() ->
                new EntityNotFoundException(MessageGenerator.generateEntityNotFoundMessage(parameter)));
    }
}
