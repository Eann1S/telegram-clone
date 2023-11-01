package com.example.repository;

import com.example.entity.Message;
import org.springframework.data.cassandra.repository.MapIdCassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MessageRepository extends MapIdCassandraRepository<Message> {

    Slice<Message> findBySenderIdAndReceiverId(Long senderId, Long receiverId, Pageable pageable);

    Optional<Message> findBySenderIdAndReceiverIdAndMessageId(Long senderId, Long receiverId, String messageId);
}
