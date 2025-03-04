package com.friendfinder.friendfindercommon.mapper;

import com.friendfinder.friendfindercommon.dto.commentDto.CommentRequestDto;
import com.friendfinder.friendfindercommon.entity.Comment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment map(CommentRequestDto requestDto);

    List<CommentRequestDto> mapToDtos(List<Comment> comments);
}
