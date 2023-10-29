package com.example.mapper;

import com.example.dto.MessageDto;
import com.example.dto.UserDto;
import com.example.dto.request.WriteMessageRequest;
import com.example.entity.Message;
import com.example.mapper.qualifier.user_id.UserByIdQualifier;
import org.instancio.junit.InstancioExtension;
import org.instancio.junit.InstancioSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith({InstancioExtension.class, MockitoExtension.class})
class MessageMapperTests {

    @Mock
    private UserByIdQualifier userByIdQualifier;
    private MessageMapper messageMapper;

    @BeforeEach
    void setUp() {
        messageMapper = new MessageMapperImpl(userByIdQualifier);
    }

    @ParameterizedTest
    @InstancioSource
    void shouldMapMessageToDto(Message message, UserDto sender, UserDto receiver) {
        when(userByIdQualifier.getUserById(message.getSenderId())).thenReturn(sender);
        when(userByIdQualifier.getUserById(message.getReceiverId())).thenReturn(receiver);

        MessageDto messageDto = messageMapper.mapMessageToDto(message);

        assertThat(messageDto)
                .extracting(MessageDto::messageId, MessageDto::sender, MessageDto::receiver, MessageDto::text, MessageDto::sendTime)
                .containsExactly(message.getMessageId(), sender, receiver, message.getText(), message.getSendTime());
    }

    @ParameterizedTest
    @InstancioSource
    void shouldMapMessagesToDtos(Message message, UserDto sender, UserDto receiver) {
        when(userByIdQualifier.getUserById(message.getSenderId())).thenReturn(sender);
        when(userByIdQualifier.getUserById(message.getReceiverId())).thenReturn(receiver);
        List<Message> messages = List.of(message);

        Iterable<MessageDto> messageDtos = messageMapper.mapMessagesToDtos(messages);

        assertThat(messageDtos)
                .flatExtracting(MessageDto::messageId, MessageDto::sender, MessageDto::receiver, MessageDto::text, MessageDto::sendTime)
                .containsExactly(message.getMessageId(), sender, receiver, message.getText(), message.getSendTime());
    }

    @ParameterizedTest
    @InstancioSource
    void shouldMapWriteMessageRequestToMessage(WriteMessageRequest writeMessageRequest) {
        Message message = messageMapper.mapWriteMessageRequestToMessage(writeMessageRequest);

        assertThat(message)
                .extracting(Message::getSenderId, Message::getReceiverId, Message::getText)
                .containsExactly(writeMessageRequest.senderId(), writeMessageRequest.receiverId(), writeMessageRequest.text());
        assertThat(message)
                .extracting(Message::getMessageId, Message::getSendTime)
                .doesNotContainNull();
    }
}