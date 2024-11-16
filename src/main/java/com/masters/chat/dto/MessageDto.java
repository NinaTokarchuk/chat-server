package com.masters.chat.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {

    private Integer id;
    private String content;
    private LocalDateTime timestamp;
    private UserDto user;
    private Integer chatId;

}
