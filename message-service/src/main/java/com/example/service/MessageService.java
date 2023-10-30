package com.example.service;

import com.example.entity.Message;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    Message saveMessageToDatabase(Message message);

    void deleteMessageFromDatabase(Message message);

    List<Message> findMessagesBySenderIdAndReceiverIdInDatabase(Long senderId, Long receiverId, Pageable pageable);

    Message findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(Long senderId, Long receiverId, String messageId);
}
