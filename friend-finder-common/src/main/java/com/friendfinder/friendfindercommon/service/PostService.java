package com.friendfinder.friendfindercommon.service;

import com.friendfinder.friendfindercommon.dto.postDto.PostRequestDto;
import com.friendfinder.friendfindercommon.dto.postDto.PostResponseDto;
import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {

    Page<Post> postFindPage(int pageNumber, CurrentUser currentUser);

    Page<Post> postFindPageVideo(int pageNumber, CurrentUser currentUser);

    Page<Post> postFindPageImage(int pageNumber, CurrentUser currentUser);

    Page<Post> postPageByUserId(int userId, int pageNumber);

    Post postSave(PostRequestDto post, CurrentUser currentUser, MultipartFile image, MultipartFile video);

    List<PostResponseDto> getAllPostFriends(int userId);

    List<Post> postUserById(int id);

    Post deletePostId(int id);

    List<Post> findAll();
}
