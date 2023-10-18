package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.request.WriteMessageRequest;
import com.example.entity.Message;
import com.example.exception.MessageNotFoundException;
import com.example.mapper.MessageMapper;
import com.example.repository.MessageRepository;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.example.message.ErrorMessage.MESSAGE_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class ChatServiceTests {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private MessageMapper messageMapper;
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatService(messageRepository, messageMapper);
    }

    @Nested
    class SuccessCases {

        @ParameterizedTest
        @InstancioSource
        void shouldWriteMessage(Message message, WriteMessageRequest request, MessageDto messageDto) {
            when(messageRepository.save(message))
                    .then(returnsFirstArg());
            when(messageMapper.mapWriteMessageRequestToMessage(request))
                    .thenReturn(message);
            when(messageMapper.mapMessageToDto(message))
                    .thenReturn(messageDto);

            MessageDto actualMessageDto = chatService.writeMessage(request);

            assertThat(actualMessageDto).isEqualTo(messageDto);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldReturnChat(List<Message> messages, List<MessageDto> messageDtos, Long senderId, Long receiverId, Pageable pageable) {
            when(messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, pageable))
                    .thenReturn(messages);
            when(messageMapper.mapMessagesToDtos(messages))
                    .thenReturn(messageDtos);

            Iterable<MessageDto> chat = chatService.getChat(senderId, receiverId, pageable);

            assertThat(chat).containsExactlyInAnyOrderElementsOf(messageDtos);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldReturnMessageFromChat(Message message, MessageDto messageDto, Long senderId, Long receiverId, String messageId) {
            when(messageRepository.findBySenderIdAndReceiverIdAndMessageId(senderId, receiverId, messageId))
                    .thenReturn(Optional.of(message));
            when(messageMapper.mapMessageToDto(message))
                    .thenReturn(messageDto);

            MessageDto messageFromChat = chatService.getMessageFromChat(senderId, receiverId, messageId);

            assertThat(messageFromChat).isEqualTo(messageDto);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldDeleteMessageFromChat(Message message, Long senderId, Long receiverId, String messageId) {
            when(messageRepository.findBySenderIdAndReceiverIdAndMessageId(senderId, receiverId, messageId))
                    .thenReturn(Optional.of(message));

            chatService.deleteMessageFromChat(senderId, receiverId, messageId);

            verify(messageRepository).delete(message);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldFindMessagesBySenderAndReceiverIdInDatabase(List<Message> messages, Long senderId, Long receiverId, Pageable pageable) {
            when(messageRepository.findBySenderIdAndReceiverId(senderId, receiverId, pageable))
                    .thenReturn(messages);

            Iterable<Message> messagesFromDb = chatService.findMessagesBySenderIdAndReceiverIdInDatabase(senderId, receiverId, pageable);

            assertThat(messagesFromDb).containsExactlyInAnyOrderElementsOf(messages);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldFindMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(Message message, Long senderId, Long receiverId, String messageId) {
            when(messageRepository.findBySenderIdAndReceiverIdAndMessageId(senderId, receiverId, messageId))
                    .thenReturn(Optional.of(message));

            Message messageFromDb = chatService.findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(senderId, receiverId, messageId);

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

            assertThatThrownBy(() -> chatService.findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(senderId, receiverId, messageId))
                    .isInstanceOf(MessageNotFoundException.class)
                    .hasMessage(MESSAGE_NOT_FOUND.formatWith(senderId, receiverId, messageId));
        }
    }
}