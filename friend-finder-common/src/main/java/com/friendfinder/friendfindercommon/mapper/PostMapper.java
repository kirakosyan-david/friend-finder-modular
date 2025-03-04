package com.friendfinder.friendfindercommon.mapper;

import com.friendfinder.friendfindercommon.dto.postDto.PostRequestDto;
import com.friendfinder.friendfindercommon.dto.postDto.PostResponseDto;
import com.friendfinder.friendfindercommon.entity.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {

    Post map(PostRequestDto requestDto);

    List<PostResponseDto> mapResp(List<Post> post);
}
