package com.example.service;

import com.example.dto.response.UserDto;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaMessageProcessor {

    private final Gson gson;
    private final UserService userService;

    void processUserRegistrationMessage(String message) {
        UserDto userDto = gson.fromJson(message, UserDto.class);
        userService.createUserFromDto(userDto);
    }
}
