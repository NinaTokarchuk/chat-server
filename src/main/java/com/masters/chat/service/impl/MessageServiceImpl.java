package com.masters.chat.service.impl;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.masters.chat.domain.Message;
import com.masters.chat.dto.MessageDto;
import com.masters.chat.dto.SendMessageRequestDto;
import com.masters.chat.dto.UserDto;
import com.masters.chat.exception.MessageNotFoundException;
import com.masters.chat.exception.UserException;
import com.masters.chat.mapper.ChatMapper;
import com.masters.chat.mapper.MessageMapper;
import com.masters.chat.mapper.UserMapper;
import com.masters.chat.repository.MessageRepository;
import com.masters.chat.service.ChatService;
import com.masters.chat.service.MessageService;
import com.masters.chat.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ChatService chatService;
    private final ChatMapper chatMapper;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;

    @Override
    public MessageDto sendMessage(SendMessageRequestDto sendMessageRequestDto) {
        var userDto = userService.findUserById(sendMessageRequestDto.getUserId());
        var user = userMapper.fromDto(userDto);
        var chatDto = chatService.findChatById(sendMessageRequestDto.getChatId());
        var chat = chatMapper.fromDto(chatDto);
        var message = Message.builder()
                .user(user)
                .chat(chat)
                .content(sendMessageRequestDto.getContent())
                .timestamp(LocalDateTime.now())
                .build();
        messageRepository.save(message);
        return messageMapper.toDto(message);
    }

    @Override
    public List<MessageDto> getChatsMessages(Integer chatId, UserDto reqUser) {
        var chat = chatService.findChatById(chatId);
        if (!chat.getUsers().contains(reqUser)) {
            throw new UserException("You are not hte member of this chat");
        }
        return messageRepository.findMessagesByChatId(chatId)
                .stream()
                .map(messageMapper::toDto)
                .collect(toList());
    }

    @Override
    public MessageDto findMessageById(Integer messageId) {
        return messageRepository.findById(messageId)
                .map(messageMapper::toDto)
                .orElseThrow(() -> new MessageNotFoundException("Message not found with id: " + messageId));

    }

    @Override
    public void deleteMessage(Integer messageId, UserDto reqUser) {
        var message = findMessageById(messageId);
        if (message.getUser().getId().equals(reqUser.getId())) {
            messageRepository.deleteById(messageId);
            return;
        }
        throw new UserException("You can not delete other user's message");
    }

}
