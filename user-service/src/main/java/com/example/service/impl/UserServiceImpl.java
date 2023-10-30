package com.example.service.impl;

import com.example.dto.UserDto;
import com.example.dto.mq_dto.RegistrationDto;
import com.example.dto.mq_dto.UpdateDto;
import com.example.dto.request.UpdateUserRequest;
import com.example.entity.User;
import com.example.exception.UserNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import com.example.service.UserMessagingService;
import com.example.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserMessagingService userMessagingService;

    @Override
    public List<UserDto> getAllUserDtos() {
        List<User> users = userRepository.findAll();
        return userMapper.mapUsersToDtos(users);
    }

    @Override
    public UserDto getUserDtoById(Long id) {
        User user = findUserByIdInDatabase(id);
        return userMapper.mapUserToDto(user);
    }

    @Override
    public UserDto getUserDtoByEmail(String email) {
        User user = findUserByEmailInDatabase(email);
        return userMapper.mapUserToDto(user);
    }

    @Override
    public void updateUserByIdFromUpdateRequest(Long id, UpdateUserRequest updateUserRequest) {
        User user = findUserByIdInDatabase(id);
        user = userMapper.updateUserFromUpdateRequest(user, updateUserRequest);
        userRepository.save(user);
        sendUpdateDtoWithUser(user);
        log.info("user {} was updated", user.getId());
    }

    @Override
    public void createUserFromRegistrationDto(RegistrationDto registrationDto) {
        User user = userMapper.mapRegistrationDtoToUser(registrationDto);
        userRepository.save(user);
        log.info("user {} was created", user.getId());
    }

    @Override
    public User findUserByIdInDatabase(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    public User findUserByEmailInDatabase(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
    }

    private void sendUpdateDtoWithUser(User user) {
        UpdateDto updateDto = userMapper.mapUserToUpdateDto(user);
        userMessagingService.send(updateDto);
    }
}
