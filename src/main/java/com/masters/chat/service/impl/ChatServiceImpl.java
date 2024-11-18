package com.masters.chat.service.impl;

import static java.util.stream.Collectors.toList;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.masters.chat.domain.Chat;
import com.masters.chat.domain.User;
import com.masters.chat.dto.ChatDto;
import com.masters.chat.dto.GroupChatRequestDto;
import com.masters.chat.exception.ChatException;
import com.masters.chat.exception.UserNotFoundException;
import com.masters.chat.mapper.ChatMapper;
import com.masters.chat.repository.ChatRepository;
import com.masters.chat.repository.UserRepository;
import com.masters.chat.service.ChatService;
import com.masters.chat.service.TokenProvider;
import com.masters.chat.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ChatMapper chatMapper;
    private final TokenProvider tokenProvider;

    @Override
    public ChatDto createChat(String token, Integer secondUserId) {
        var email = tokenProvider.getEmailFromToken(token);
        var reqUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
        var user = userRepository.findById(secondUserId)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + secondUserId));
        var chat = Optional.ofNullable(chatRepository.findSingleChatByUserIds(user, reqUser))
                .orElseGet(() -> Chat.builder()
                        .createdBy(reqUser)
                        .users(Set.of(user, reqUser))
                        .isGroup(false)
                        .build());
        chatRepository.save(chat);
        return chatMapper.toDto(chat);
    }

    @Override
    public ChatDto findChatById(Integer chatId) {
        return chatRepository.findById(chatId)
                .map(chatMapper::toDto)
                .orElseThrow(() -> new ChatException("Chat not found with id: " + chatId));
    }

    @Override
    public List<ChatDto> findAllChatsByUserId(Integer userId) {
        return chatRepository.findChatByUserId(userId).stream().map(chatMapper::toDto).collect(toList());
    }

    @Override
    public ChatDto createGroup(GroupChatRequestDto groupChatRequest, String token) {
        var email = tokenProvider.getEmailFromToken(token);
        var reqUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
        var addedUsers = userRepository.findAllById(groupChatRequest.getUserIds());
        addedUsers.add(reqUser);
        var users = new HashSet(addedUsers);
        var group = Chat.builder()
                .isGroup(true)
                .chatImage(groupChatRequest.getChatImage())
                .chatName(groupChatRequest.getChatName())
                .createdBy(reqUser)
                .users(users)
                .build();
        chatRepository.save(group);
        return chatMapper.toDto(group);
    }

    @Override
    public ChatDto addUserToGroup(Integer userId, Integer chatId, String token) {
        var email = tokenProvider.getEmailFromToken(token);
        var reqUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + userId));
        return chatRepository.findById(chatId)
                .map(chat -> addUserToGroupIfAdmin(chat, reqUser, user))
                .orElseThrow(() -> new ChatException("No chat found with id: " + chatId));

    }

    private ChatDto addUserToGroupIfAdmin(Chat chat, User reqUser, User user) {
        if (chat.getCreatedBy().equals(reqUser)) {
            chat.getUsers().add(user);
            log.info("User with id: {} successfully added to chat with id: {}", user.getId(), chat.getId());
            chatRepository.save(chat);
        }
        log.info("User with id {} is not admin, so he can't add users to group", reqUser.getId());
        return chatMapper.toDto(chat);
    }

    @Override
    public ChatDto renameGroup(Integer chatId, String groupName, String token) {
        var email = tokenProvider.getEmailFromToken(token);
        var reqUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
        return chatRepository.findById(chatId)
                .map(chat -> renameGroupIfParticipant(chat, groupName, reqUser))
                .orElseThrow(() -> new ChatException("No chat found with id: " + chatId));
    }

    private ChatDto renameGroupIfParticipant(Chat chat, String groupName, User reqUser) {
        if (chat.getCreatedBy().equals(reqUser)) {
            chat.setChatName(groupName);
            log.info("Group with id: {} successfully renamed to {} by user with id {} ",
                    chat.getId(), groupName, reqUser.getId());
            chatRepository.save(chat);
        }
        log.info("User with id {} is not member of group with id {}, so he can't rename the group",
                reqUser.getId(), chat.getId());
        return chatMapper.toDto(chat);
    }

    @Override
    public ChatDto removeFromGroup(Integer chatId, Integer userId, String token) {
        var email = tokenProvider.getEmailFromToken(token);
        var reqUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found by email: " + email));
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found by id: " + userId));
        return chatRepository.findById(chatId)
                .map(chat -> removeUserFromGroupIfAdminOrSelfDelete(chat, reqUser, user))
                .orElseThrow(() -> new ChatException("No chat found with id: " + chatId));
    }

    private ChatDto removeUserFromGroupIfAdminOrSelfDelete(Chat chat, User reqUser, User user) {
        if (chat.getCreatedBy().equals(reqUser)) {
            chat.getUsers().remove(user);
            log.info("User with id: {} successfully removed from chat with id: {}", user.getId(), chat.getId());
            chatRepository.save(chat);
        } else if (chat.getUsers().contains(reqUser)) {
            if (user.getId().equals(reqUser.getId())) {
                chat.getUsers().remove(user);
                log.info("User with id: {} successfully removed from chat with id: {}", user.getId(), chat.getId());
                chatRepository.save(chat);
            }
        }
        log.info("User with id {} is not admin, so he can't add users to group", reqUser.getId());
        return chatMapper.toDto(chat);
    }

    @Override
    public void deleteChat(Integer chatId, Integer userId) {
        chatRepository.findById(chatId)
                .ifPresent(chat -> chatRepository.deleteById(chatId));
    }
}
