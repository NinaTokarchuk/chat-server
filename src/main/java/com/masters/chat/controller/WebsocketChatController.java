package com.masters.chat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.masters.chat.dto.MessageDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WebsocketChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @MessageMapping("/message")
    @SendTo("/group/public")
    public MessageDto receiveMessage(@Payload MessageDto message) {
        log.info("Received message through websocket: {}", message.getContent());
        simpMessagingTemplate.convertAndSend("/group/" + message.getChatId().toString(), message);
        return message;
    }

}
