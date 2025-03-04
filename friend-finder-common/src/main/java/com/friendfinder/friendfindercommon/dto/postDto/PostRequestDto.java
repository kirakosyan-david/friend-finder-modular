package com.friendfinder.friendfindercommon.dto.postDto;

import com.friendfinder.friendfindercommon.entity.User;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostRequestDto {


    private String description;
    private String imgName;
    private String musicFileName;
    private Date postDatetime;
    private User user;
}
