package com.friendfinder.friendfindercommon.dto.commentDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentRequestDto {
    private int userId;
    private int postId;
    private LocalDateTime datetime;
    private String commentaryText;
}
