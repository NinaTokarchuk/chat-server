package com.masters.chat.service;

import java.util.List;

import com.masters.chat.domain.Chat;
import com.masters.chat.domain.User;
import com.masters.chat.dto.ChatDto;
import com.masters.chat.dto.GroupChatRequestDto;

public interface ChatService {

    ChatDto createChat(String token, Integer secondUserId);

    ChatDto findChatById(Integer chatId);

    List<ChatDto> findAllChatsByUserId(Integer userId);

    ChatDto createGroup(GroupChatRequestDto groupChatRequest, String token);

    ChatDto addUserToGroup(Integer userId, Integer chatId, String token);

    ChatDto renameGroup(Integer chatId, String groupName, String token);

    ChatDto removeFromGroup(Integer chatId, Integer userId, String token);

    void deleteChat(Integer chatId, Integer userId);

}
