package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.request.WriteMessageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatService {

    MessageDto writeMessage(WriteMessageRequest writeMessageRequest);

    List<MessageDto> getChat(Long userId, Long friendId, Pageable pageable);

    MessageDto getMessageFromChat(Long userId, Long friendId, String messageId);

    void deleteMessageFromChat(Long userId, Long friendId, String messageId);
}
