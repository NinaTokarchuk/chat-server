package com.masters.chat.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatRequestDto {

    private Set<Integer> userIds;
    private String chatName;
    private String chatImage;

}
