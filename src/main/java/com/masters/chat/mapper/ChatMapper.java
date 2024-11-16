package com.masters.chat.mapper;

import org.mapstruct.Mapper;

import com.masters.chat.domain.Chat;
import com.masters.chat.dto.ChatDto;

@Mapper(config = CommonMapperConfig.class, uses = {MessageMapper.class, UserMapper.class})
public interface ChatMapper {

    ChatDto toDto(Chat chat);

    Chat fromDto(ChatDto chatDto);

}
