package com.masters.chat.dto;

import java.util.List;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

    private Integer id;
    private Set<UserDto> admins;
    private String chatName;
    private String chatImage;
    private boolean isGroup;
    private UserDto createdBy;
    private Set<UserDto> users;
    private List<MessageDto> messages;

}
