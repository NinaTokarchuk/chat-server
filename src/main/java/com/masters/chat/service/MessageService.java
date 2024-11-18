package com.masters.chat.service;

import java.util.List;

import com.masters.chat.dto.MessageDto;
import com.masters.chat.dto.SendMessageRequestDto;
import com.masters.chat.dto.UserDto;

public interface MessageService {

    MessageDto sendMessage(SendMessageRequestDto sendMessageRequestDto);

    List<MessageDto> getMessagesByChatId(Integer chatId, UserDto reqUser);

    MessageDto findMessageById(Integer messageId);

    void deleteMessage(Integer messageId, UserDto reqUser);

}
