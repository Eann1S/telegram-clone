package com.example.client;

import com.example.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "${services.user-service.name}")
@Component
public interface UserServiceClient {

    @GetMapping("/api/v1/user/{id}")
    UserDto getUserById(@PathVariable Long id);
}
