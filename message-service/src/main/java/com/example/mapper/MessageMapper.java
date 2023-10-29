package com.example.mapper;

import com.example.dto.MessageDto;
import com.example.dto.request.WriteMessageRequest;
import com.example.entity.Message;
import com.example.mapper.qualifier.user_id.UserById;
import com.example.mapper.qualifier.user_id.UserByIdQualifier;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = UserByIdQualifier.class,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface MessageMapper {

    @Mapping(target = "sender", source = "senderId", qualifiedBy = UserById.class)
    @Mapping(target = "receiver", source = "receiverId", qualifiedBy = UserById.class)
    MessageDto mapMessageToDto(Message message);

    List<MessageDto> mapMessagesToDtos(Iterable<Message> messages);

    @Mapping(target = "messageId", expression = "java(java.util.UUID.randomUUID().toString())")
    @Mapping(target = "sendTime", expression = "java(java.time.LocalDateTime.now())")
    Message mapWriteMessageRequestToMessage(WriteMessageRequest writeMessageRequest);
}
