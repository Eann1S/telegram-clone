package com.example.mapper.qualifier.user_id;

import com.example.client.UserServiceClient;
import com.example.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserByIdQualifier {

    private final UserServiceClient userServiceClient;

    @UserById
    public UserDto getUserById(Long id) {
        return userServiceClient.getUserById(id);
    }
}
