package com.masters.chat.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.masters.chat.domain.Message;
import com.masters.chat.dto.MessageDto;

@Mapper(config = CommonMapperConfig.class, uses = {UserMapper.class})
public interface MessageMapper {

    @Mapping(target = "chatId", source = "chat.id")
    MessageDto toDto(Message message);

    List<MessageDto> toDtos(List<Message> messages);

    @Mapping(source = "chatId", target = "chat.id")
    Message fromDto(MessageDto messageDto);

    List<Message> fromDtos(List<MessageDto> messageDtos);

}
