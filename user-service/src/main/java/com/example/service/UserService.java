package com.example.service;

import com.example.dto.mq_dto.RegistrationDto;
import com.example.dto.mq_dto.UpdateDto;
import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.UserDto;
import com.example.entity.User;
import com.example.exception.EntityNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.messaging.UserMessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserMessagingService userMessagingService;

    public Collection<UserDto> getAllUsers() {
        Collection<User> users = userRepository.findAll();
        return userMapper.mapUsersToDtos(users);
    }

    public UserDto getUserById(Long id) {
        User user = findUserByIdInDatabase(id);
        return userMapper.mapUserToDto(user);
    }

    public UserDto getUserByEmail(String email) {
        User user = findUserByEmailInDatabase(email);
        return userMapper.mapUserToDto(user);
    }

    public void updateUserByIdFromUpdateRequest(Long id, UpdateUserRequest updateUserRequest) {
        User user = findUserByIdInDatabase(id);
        user = userMapper.updateUserFieldsFromUpdateRequest(user, updateUserRequest);
        userRepository.save(user);
        sendUpdateDtoWithUser(user);
        log.info("user {} was updated", user.getId());
    }

    public void createUserFrom(RegistrationDto registrationDto) {
        User user = userMapper.mapRegistrationDtoToUser(registrationDto);
        userRepository.save(user);
        log.info("user {} was created", user.getId());
    }

    User findUserByIdInDatabase(Long id) {
        return userRepository.findById(id)
                .orElseThrow(supplyExceptionIfUserDoesNotExist(id));
    }

    User findUserByEmailInDatabase(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(supplyExceptionIfUserDoesNotExist(email));
    }

    private void sendUpdateDtoWithUser(User user) {
        UpdateDto updateDto = userMapper.mapUserToUpdateDto(user);
        userMessagingService.send(updateDto);
    }

    private Supplier<RuntimeException> supplyExceptionIfUserDoesNotExist(Object userProperty) {
        return () -> new EntityNotFoundException(userProperty);
    }
}
