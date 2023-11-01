package com.example.service;

import com.example.entity.Message;
import com.example.exception.MessageNotFoundException;
import com.example.repository.MessageRepository;
import com.example.service.impl.MessageServiceImpl;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SliceImpl;

import java.util.List;
import java.util.Optional;

import static com.example.message.ErrorMessage.MESSAGE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
public class MessageServiceImplTests {

    @Mock
    private MessageRepository messageRepository;
    private MessageService messageService;

    @BeforeEach
    void setUp() {
        messageService = new MessageServiceImpl(messageRepository);
    }

    @Nested
    class SuccessCases {

        @ParameterizedTest
        @InstancioSource
        void shouldSaveMessageToDatabase(Message message) {
            when(messageRepository.save(message))
                    .thenReturn(message);

            Message savedMessage = messageService.saveMessageToDatabase(message);

            assertThat(savedMessage).isEqualTo(message);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldDeleteMessageFromDatabase(Message message) {
            messageService.deleteMessageFromDatabase(message);

            verify(messageRepository).delete(message);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldFindMessagesBySenderAndReceiverIdInDatabase(List<Message> messages, Long senderId, Long receiverId) {
            when(messageRepository.findBySenderIdAndReceiverId(eq(senderId), eq(receiverId), any(Pageable.class)))
                    .thenReturn(new SliceImpl<>(messages));

            Iterable<Message> messagesFromDb = messageService.findMessagesBySenderIdAndReceiverIdInDatabase(
                    senderId, receiverId, Pageable.ofSize(5));

            assertThat(messagesFromDb).containsExactlyInAnyOrderElementsOf(messages);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldFindMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(Message message, Long senderId, Long receiverId, String messageId) {
            when(messageRepository.findBySenderIdAndReceiverIdAndMessageId(senderId, receiverId, messageId))
                    .thenReturn(Optional.of(message));

            Message messageFromDb = messageService.findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(senderId, receiverId, messageId);

            assertThat(messageFromDb).isEqualTo(message);
        }
    }

    @Nested
    class FailureCases {

        @ParameterizedTest
        @InstancioSource
        void shouldThrowExceptionWhenMessageWasNotFound(Long senderId, Long receiverId, String messageId) {
            when(messageRepository.findBySenderIdAndReceiverIdAndMessageId(senderId, receiverId, messageId))
                    .thenReturn(Optional.empty());

            assertThatThrownBy(() -> messageService.findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(senderId, receiverId, messageId))
                    .isInstanceOf(MessageNotFoundException.class)
                    .hasMessage(MESSAGE_NOT_FOUND.formatWith(senderId, receiverId, messageId));
        }
    }
}
