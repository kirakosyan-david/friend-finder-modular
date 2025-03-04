package com.friendfinder.friendfindercommon.mapper;

import com.friendfinder.friendfindercommon.dto.postLikeDto.PostLikeDto;
import com.friendfinder.friendfindercommon.entity.PostLike;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PostLikeMapper {

    PostLike map(PostLikeDto requestDto);
}
