package com.example.mapper;

import com.example.client.UserServiceClient;
import com.example.dto.MessageDto;
import com.example.dto.request.WriteMessageRequest;
import com.example.entity.Message;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = UserServiceClient.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MessageMapper {

    @Mapping(target = "sender", source = "senderId")
    @Mapping(target = "receiver", source = "receiverId")
    MessageDto mapMessageToDto(Message message);

    List<MessageDto> mapMessagesToDtos(Iterable<Message> messages);

    @Mapping(target = "messageId", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "sendTime", expression = "java(java.time.LocalDateTime.now())")
    Message mapWriteMessageRequestToMessage(WriteMessageRequest writeMessageRequest);
}
