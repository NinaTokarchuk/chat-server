package com.masters.chat.controller;

import static com.masters.chat.constants.Constants.AUTHORIZATION_TOKEN_HEADER;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.masters.chat.dto.ApiResponse;
import com.masters.chat.dto.MessageDto;
import com.masters.chat.dto.SendMessageRequestDto;
import com.masters.chat.service.MessageService;
import com.masters.chat.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;
    private final UserService userService;

    @PostMapping("/create")
    public MessageDto createMessage(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token,
                                    @RequestBody SendMessageRequestDto sendMessageRequestDto) {
        var user = userService.findUserByProfile(token);
        sendMessageRequestDto.setUserId(user.getId());
        return messageService.sendMessage(sendMessageRequestDto);
    }

    @GetMapping("/chats/{chatId}")
    public List<MessageDto> getMessagesByChatId(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token,
                                                @PathVariable Integer chatId) {
        var user = userService.findUserByProfile(token);
        return messageService.getMessagesByChatId(chatId, user);
    }

    @DeleteMapping("/{messageId}")
    public ApiResponse deleteMessage(@RequestHeader(AUTHORIZATION_TOKEN_HEADER) String token,
                                     @PathVariable Integer messageId) {
        var user = userService.findUserByProfile(token);
        messageService.deleteMessage(messageId, user);
        return ApiResponse.builder()
                .message("Message is deleted successfully")
                .status(true)
                .build();
    }

}
