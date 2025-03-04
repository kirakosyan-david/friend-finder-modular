package com.friendfinder.friendfindercommon.dto.postDto;

import com.friendfinder.friendfindercommon.entity.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponseDto {


    private int id;
    private String description;
    private String imgName;
    private String musicFileName;
    private int likeCount;
    private int dislikeCount;
    private Date postDatetime;
    private User user;
}
