package com.example.controller;

import com.example.dto.request.UpdateUserRequest;
import com.example.dto.response.MessageDto;
import com.example.dto.response.UserDto;
import com.example.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

import static com.example.message.InfoMessage.USER_UPDATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<Collection<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping(value = "/user", params = "email")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam String email) {
        return ResponseEntity.ok(userService.getUserByEmail(email));
    }

    @PutMapping("/user/{id}/update")
    public ResponseEntity<MessageDto> updateUserById(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest updateUserRequest
    ) {
        userService.updateUserByIdFromUpdateRequest(id, updateUserRequest);
        return ResponseEntity.ok(
                MessageDto.of(USER_UPDATED));
    }
}
