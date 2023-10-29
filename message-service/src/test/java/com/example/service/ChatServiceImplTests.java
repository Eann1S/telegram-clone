package com.example.service;

import com.example.dto.MessageDto;
import com.example.dto.request.WriteMessageRequest;
import com.example.entity.Message;
import com.example.mapper.MessageMapper;
import com.example.service.impl.ChatServiceImpl;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, InstancioExtension.class})
class ChatServiceImplTests {

    @Mock
    private MessageService messageService;
    @Mock
    private MessageMapper messageMapper;
    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatServiceImpl(messageService, messageMapper);
    }

    @Nested
    class SuccessCases {

        @ParameterizedTest
        @InstancioSource
        void shouldWriteMessage(Message message, WriteMessageRequest request, MessageDto messageDto) {
            when(messageService.createMessageFromWriteMessageRequest(request))
                    .thenReturn(message);
            when(messageMapper.mapMessageToDto(message))
                    .thenReturn(messageDto);

            MessageDto actualMessageDto = chatService.writeMessage(request);

            assertThat(actualMessageDto).isEqualTo(messageDto);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldReturnChat(List<Message> messages, List<MessageDto> messageDtos, Long senderId, Long receiverId, Pageable pageable) {
            when(messageService.findMessagesBySenderIdAndReceiverIdInDatabase(senderId, receiverId, pageable))
                    .thenReturn(messages);
            when(messageMapper.mapMessagesToDtos(messages))
                    .thenReturn(messageDtos);

            Iterable<MessageDto> chat = chatService.getChat(senderId, receiverId, pageable);

            assertThat(chat).containsExactlyInAnyOrderElementsOf(messageDtos);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldReturnMessageFromChat(Message message, MessageDto messageDto, Long senderId, Long receiverId, String messageId) {
            when(messageService.findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(senderId, receiverId, messageId))
                    .thenReturn(message);
            when(messageMapper.mapMessageToDto(message))
                    .thenReturn(messageDto);

            MessageDto messageFromChat = chatService.getMessageFromChat(senderId, receiverId, messageId);

            assertThat(messageFromChat).isEqualTo(messageDto);
        }

        @ParameterizedTest
        @InstancioSource
        void shouldDeleteMessageFromChat(Message message, Long senderId, Long receiverId, String messageId) {
            when(messageService.findMessageBySenderIdAndReceiverIdAndMessageIdInDatabase(senderId, receiverId, messageId))
                    .thenReturn(message);

            chatService.deleteMessageFromChat(senderId, receiverId, messageId);

            verify(messageService).deleteMessage(message);
        }
    }
}