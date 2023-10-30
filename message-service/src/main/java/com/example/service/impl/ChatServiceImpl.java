package com.example.service.impl;

import com.example.dto.MessageDto;
import com.example.dto.request.WriteMessageRequest;
import com.example.entity.Message;
import com.example.mapper.MessageMapper;
import com.example.service.ChatService;
import com.example.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatServiceImpl implements ChatService {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @Override
    public MessageDto writeMessage(WriteMessageRequest writeMessageRequest) {
        Message message = messageMapper.mapWriteMessageRequestToMessage(writeMessageRequest);
        message = messageService.saveMessageToDatabase(message);
        log.info("In partition {{}:{}} message {} was created", message.getSenderId(), message.getReceiverId(), message.getMessageId());
        return messageMapper.mapMessageToDto(message);
    }

    @Override
    public List<MessageDto> getChat(Long userId, Long friendId, Pageable pageable) {
        List<Message> messages = messageService.findMessagesBySenderIdAndReceiverIdInDatabase(userId, friendId, pageable);
        return messageMapper.mapMessagesToDtos(messages);
    }

    @Override
    public MessageDto getMessageFromChat(Long userId, Long friendId, String messageId) {
        Message message = messageService.findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(userId, friendId, messageId);
        return messageMapper.mapMessageToDto(message);
    }

    @Override
    public void deleteMessageFromChat(Long userId, Long friendId, String messageId) {
        Message message = messageService.findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(userId, friendId, messageId);
        messageService.deleteMessageFromDatabase(message);
        log.info("In partition {{}:{}} message {} was deleted", message.getSenderId(), message.getReceiverId(), message.getMessageId());
    }
}
