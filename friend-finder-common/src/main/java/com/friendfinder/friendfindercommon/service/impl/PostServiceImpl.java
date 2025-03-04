package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.dto.postDto.PostRequestDto;
import com.friendfinder.friendfindercommon.dto.postDto.PostResponseDto;
import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.mapper.PostMapper;
import com.friendfinder.friendfindercommon.repository.PostRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.PostService;
import com.friendfinder.friendfindercommon.service.UserActivityService;
import com.friendfinder.friendfindercommon.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * <p>
 * PostServiceImpl is a service class that provides methods to handle posts in the application.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>postRepository: The PostRepository interface used to access and save post-related data to the database.</li>
 *     <li>userRepository: The UserRepository interface used to access and retrieve user-related data from the database.</li>
 *     <li>friendRequestService: The FriendRequestService interface used to manage friend requests and retrieve friends of a user.</li>
 *     <li>postMapper: The PostMapper interface used for mapping between post-related DTOs and entities.</li>
 *     <li>userActivityService: The UserActivityService interface used to manage and save user activity data.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>postFindPage(pageNumber, currentUser): Retrieves a page of posts from friends of the current user.
 *     It fetches the posts from users who are friends of the current user based on the pageNumber and currentUser information.
 *     The method returns a Page object containing the post data.</li>
 *     <li>postFindPageVideo(pageNumber, currentUser): Retrieves a page of video posts from friends of the current user.
 *     Similar to postFindPage(), this method fetches video posts specifically and returns a Page object containing the post data.</li>
 *     <li>postFindPageImage(pageNumber, currentUser): Retrieves a page of image posts from friends of the current user.
 *     Similar to postFindPage(), this method fetches image posts specifically and returns a Page object containing the post data.</li>
 *     <li>postPageByUserId(userId, pageNumber): Retrieves a page of posts by a specific user.
 *     It fetches the posts made by the user with the given userId based on the pageNumber and returns a Page object containing the post data.</li>
 *     <li>postSave(requestDto, currentUser, image, video): Saves a new post to the database based on the information provided in the PostRequestDto object.
 *     The method validates the input data, uploads images and videos to the server, and then saves the post with the current timestamp and user information.</li>
 *     <li>getAllPostFriends(userId): Retrieves a list of posts made by friends of the user with the given userId.
 *     The method fetches the posts made by all friends of the user and returns a list of PostResponseDto objects containing the post data.</li>
 *     <li>findAll(): Retrieves a list of all posts available in the database and returns it.</li>
 *     <li>postUserById(id): Retrieves a list of posts made by a user with the given id.
 *     The method fetches the posts made by the user with the given id and returns a list of Post objects containing the post data.</li>
 *     <li>deletePostId(id): Deletes a post with the given id from the database if it exists.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * PostServiceImpl provides the core functionality for handling posts in the application. It allows users to create and save
 * new posts with images and videos, retrieve posts from friends, and manage post data in the database. The methods in this
 * class are extensively used by the application to display posts, save new posts, and manage user interactions with posts.
 * The implementation is designed to handle post-related data efficiently and provide a seamless user experience for managing
 * and viewing posts in the social media application.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FriendRequestService friendRequestService;
    private final PostMapper postMapper;
    private final UserActivityService userActivityService;

    @Value("${post.upload.image.path}")
    private String postImageUploadPath;

    @Value("${post.video.upload.image.path}")
    private String postVideoUploadPath;

    @Override
    public Page<Post> postFindPage(int pageNumber, CurrentUser currentUser) {
        List<PostResponseDto> allPostFriends = getAllPostFriends(currentUser.getUser().getId());
        List<Integer> friendIds = new ArrayList<>();
        for (PostResponseDto post : allPostFriends) {
            friendIds.add(post.getUser().getId());
        }
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageNumber - 1, 5, sort);

        return postRepository.findByUserIdIn(friendIds, pageable);
    }

    @Override
    public Page<Post> postFindPageVideo(int pageNumber, CurrentUser currentUser) {
        List<PostResponseDto> allPostFriends = getAllPostFriends(currentUser.getUser().getId());
        List<String> videos = new ArrayList<>();
        for (PostResponseDto post : allPostFriends) {
            videos.add(post.getMusicFileName());
        }
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, sort);

        return postRepository.findPostsByMusicFileNameIn(videos, pageable);
    }

    @Override
    public Page<Post> postFindPageImage(int pageNumber, CurrentUser currentUser) {
        List<PostResponseDto> allPostFriends = getAllPostFriends(currentUser.getUser().getId());
        List<String> images = new ArrayList<>();
        for (PostResponseDto post : allPostFriends) {
            images.add(post.getImgName());
        }
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageNumber - 1, 10, sort);

        return postRepository.findPostsByImgNameIn(images, pageable);
    }

    @Override
    public Page<Post> postPageByUserId(int userId, int pageNumber) {
        List<Post> posts = postUserById(userId);
        List<Integer> postId = new ArrayList<>();
        for (Post post : posts) {
            postId.add(post.getUser().getId());
        }
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageNumber - 1, 5, sort);
        return postRepository.findPostsByUserIdIn(postId, pageable);
    }

    @Override
    public Post postSave(PostRequestDto requestDto, CurrentUser currentUser, MultipartFile image, MultipartFile video) {
        String imgName = ImageUtil.uploadImage(image, postImageUploadPath);
        String musicFileName = ImageUtil.uploadImage(video, postVideoUploadPath);
        Post post = postMapper.map(PostRequestDto.builder()
                .imgName(imgName)
                .musicFileName(musicFileName)
                .postDatetime(new Date())
                .description(requestDto.getDescription())
                .user(currentUser.getUser())
                .build());
        if (imgName != null) {
            userActivityService.save(currentUser.getUser(), "posted a photo");
        } else {
            userActivityService.save(currentUser.getUser(), "posted a video");
        }
        return postRepository.save(post);
    }

    @Override
    public List<PostResponseDto> getAllPostFriends(int userId) {
        List<User> friendsByUserId = friendRequestService.findFriendsByUserId(userId);
        List<Integer> friendsIds = friendsByUserId
                .stream()
                .map(User::getId)
                .toList();

        List<PostResponseDto> postList = new ArrayList<>();
        for (Integer friendsId : friendsIds) {
            postList.addAll(postMapper.mapResp(postRepository.findByUserId(friendsId)));
        }

        return postList;
    }

    @Override
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> postUserById(int id) {
        Optional<User> byId = userRepository.findById(id);
        if (byId.isPresent()) {
            User user = byId.get();
            return postRepository.findByUserId(user.getId());
        }
        return Collections.emptyList();

    }

    @Override
    public Post deletePostId(int id) {
        Optional<Post> byId = postRepository.findById(id);
        if (byId.isPresent()) {
            Post post = byId.get();
            postRepository.deleteById(post.getId());
        }
        return null;
    }
}
