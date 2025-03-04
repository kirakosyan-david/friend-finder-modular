package com.friendfinder.friendfindercommon.dto.postLikeDto;

import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.LikeStatus;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostLikeDto {

    private int id;
    private LikeStatus likeStatus;
    private Post post;
    private User user;
}
