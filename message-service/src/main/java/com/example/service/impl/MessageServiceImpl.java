package com.example.service.impl;

import com.example.dto.request.WriteMessageRequest;
import com.example.entity.Message;
import com.example.exception.MessageNotFoundException;
import com.example.mapper.MessageMapper;
import com.example.repository.MessageRepository;
import com.example.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    @Override
    public Message createMessageFromWriteMessageRequest(WriteMessageRequest writeMessageRequest) {
        Message message = messageMapper.mapWriteMessageRequestToMessage(writeMessageRequest);
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessage(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public Message findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(Long senderId, Long receiverId, String messageId) {
        return messageRepository.findBySenderIdAndReceiverIdAndMessageId(senderId, receiverId, messageId)
                .orElseThrow(() -> new MessageNotFoundException(senderId, receiverId, messageId));
    }

    @Override
    public List<Message> findMessagesBySenderIdAndReceiverIdInDatabase(Long senderId, Long receiverId, Pageable pageable) {
        return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, pageable);
    }
}
