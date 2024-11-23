package com.masters.chat.controller;

import static com.masters.chat.constants.Constants.AUTHORIZATION_TOKEN_HEADER;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masters.chat.dto.ApiResponse;
import com.masters.chat.dto.ChatDto;
import com.masters.chat.dto.GroupChatRequestDto;
import com.masters.chat.dto.SingleChatRequestDto;
import com.masters.chat.service.ChatService;
import com.masters.chat.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    @PostMapping("/single")
    public ChatDto createChat(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token,
                              @RequestBody SingleChatRequestDto singleChatRequestDto) {
        return chatService.createChat(token, singleChatRequestDto.getUserId());
    }

    @PostMapping("/group")
    public ChatDto createGroup(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token,
                               @RequestBody GroupChatRequestDto groupChatRequestDto) {
        return chatService.createGroup(groupChatRequestDto, token);
    }

    @GetMapping("/{chatId}")
    public ChatDto findChatById(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token,
                                @PathVariable Integer chatId) {
        return chatService.findChatById(chatId);
    }

    @GetMapping("/user")
    public List<ChatDto> findAllChatsByUserId(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token) {
        var reqUser = userService.findUserByProfile(token);
        return chatService.findAllChatsByUserId(reqUser.getId());
    }

    @PutMapping("/{chatId}/add/{userId}")
    public ChatDto addUserToGroup(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token,
                                  @PathVariable Integer chatId,
                                  @PathVariable Integer userId) {
        return chatService.addUserToGroup(userId, chatId, token);
    }

    @PutMapping("/{chatId}/remove/{userId}")
    public ChatDto removeFromGroup(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token,
                                   @PathVariable Integer chatId,
                                   @PathVariable Integer userId) {
        return chatService.removeFromGroup(chatId, userId, token);
    }

    @DeleteMapping("/delete/{chatId}")
    public ApiResponse deleteChat(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token,
                                  @PathVariable Integer chatId) {
        var reqUser = userService.findUserByProfile(token);
        chatService.deleteChat(chatId, reqUser.getId());
        return ApiResponse.builder()
                .message("Chat is deleted successfully")
                .status(true)
                .build();
    }

}
