package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.request.WriteMessageRequest;
import com.example.entity.Message;
import com.example.exception.MessageNotFoundException;
import com.example.mapper.MessageMapper;
import com.example.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    public MessageDto writeMessage(WriteMessageRequest writeMessageRequest) {
        Message message = messageMapper.mapWriteMessageRequestToMessage(writeMessageRequest);
        message = messageRepository.save(message);
        log.info("In partition {{}:{}} message {} was created", message.getSenderId(), message.getReceiverId(), message.getMessageId());
        return messageMapper.mapMessageToDto(message);
    }

    public List<MessageDto> getChat(Long userId, Long friendId, Pageable pageable) {
        List<Message> messages = findMessagesBySenderIdAndReceiverIdInDatabase(userId, friendId, pageable);
        return messageMapper.mapMessagesToDtos(messages);
    }

    public MessageDto getMessageFromChat(Long userId, Long friendId, String messageId) {
        Message message = findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(userId, friendId, messageId);
        return messageMapper.mapMessageToDto(message);
    }

    public void deleteMessageFromChat(Long userId, Long friendId, String messageId) {
        Message message = findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(userId, friendId, messageId);
        messageRepository.delete(message);
        log.info("In partition {{}:{}} message {} was deleted", message.getSenderId(), message.getReceiverId(), message.getMessageId());
    }

    List<Message> findMessagesBySenderIdAndReceiverIdInDatabase(Long senderId, Long receiverId, Pageable pageable) {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, pageable);
    }

    Message findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(Long senderId, Long receiverId, String messageId) {
        return messageRepository.findBySenderIdAndReceiverIdAndMessageId(senderId, receiverId, messageId)
                .orElseThrow(() -> new MessageNotFoundException(senderId, receiverId, messageId));
    }
}
