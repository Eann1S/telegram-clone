package com.example.service.impl;

import com.example.entity.Message;
import com.example.exception.MessageNotFoundException;
import com.example.repository.MessageRepository;
import com.example.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.naturalOrder;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;

    @Override
    public Message saveMessageToDatabase(Message message) {
        return messageRepository.save(message);
    }

    @Override
    public void deleteMessageFromDatabase(Message message) {
        messageRepository.delete(message);
    }

    @Override
    public Message findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(Long senderId, Long receiverId, String messageId) {
        return messageRepository.findBySenderIdAndReceiverIdAndMessageId(senderId, receiverId, messageId)
                .orElseThrow(() -> new MessageNotFoundException(senderId, receiverId, messageId));
    }

    @Override
    public List<Message> findMessagesBySenderIdAndReceiverIdInDatabase(Long senderId, Long receiverId, Pageable pageable) {
        PageRequest pageRequest = createPageRequestWithSortingFromPageable(pageable);
        List<Message> messages = messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, pageRequest).getContent();
        return sortMessages(messages);
    }

    @NotNull
    private List<Message> sortMessages(List<Message> messages) {
        return messages.stream()
                .sorted(naturalOrder())
                .collect(Collectors.toList());
    }

    private PageRequest createPageRequestWithSortingFromPageable(Pageable pageable) {
        return CassandraPageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
    }
}
