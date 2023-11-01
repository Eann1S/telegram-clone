package com.example.controller;

import com.example.dto.MessageDto;
import com.example.dto.request.WriteMessageRequest;
import com.example.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/{friendId}/write")
    public ResponseEntity<MessageDto> writeMessage(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long friendId,
            @RequestBody String text
    ) {
        WriteMessageRequest writeMessageRequest = WriteMessageRequest.of(userId, friendId, text);
        MessageDto messageDto = chatService.writeMessage(writeMessageRequest);
        return new ResponseEntity<>(messageDto, CREATED);
    }

    @GetMapping("/{friendId}")
    public ResponseEntity<List<MessageDto>> getChat(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long friendId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        List<MessageDto> chat = chatService.getChat(userId, friendId, pageable);
        return ResponseEntity.ok(chat);
    }

    @GetMapping("/{friendId}/message/{messageId}")
    public ResponseEntity<MessageDto> getMessageFromChat(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long friendId,
            @PathVariable String messageId
    ) {
        MessageDto messageDto = chatService.getMessageFromChat(userId, friendId, messageId);
        return ResponseEntity.ok(messageDto);
    }

    @DeleteMapping("/{friendId}/message/{messageId}/delete")
    public ResponseEntity<Void> deleteMessageFromChat(
            @RequestHeader("User-Id") Long userId,
            @PathVariable Long friendId,
            @PathVariable String messageId
    ) {
        chatService.deleteMessageFromChat(userId, friendId, messageId);
        return ResponseEntity.ok().build();
    }
}
