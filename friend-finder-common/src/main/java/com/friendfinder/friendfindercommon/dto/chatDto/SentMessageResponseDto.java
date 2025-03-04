package com.friendfinder.friendfindercommon.dto.chatDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SentMessageResponseDto {
    private int senderId;
    private int receiverId;
    private String content;
}
