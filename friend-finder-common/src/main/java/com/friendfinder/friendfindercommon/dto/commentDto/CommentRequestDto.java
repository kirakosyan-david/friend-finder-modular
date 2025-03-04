package com.friendfinder.friendfindercommon.dto.commentDto;

import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.entity.User;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentRequestDto {

    private User user;
    private Post post;
    private LocalDateTime datetime;
    private String commentaryText;
}
