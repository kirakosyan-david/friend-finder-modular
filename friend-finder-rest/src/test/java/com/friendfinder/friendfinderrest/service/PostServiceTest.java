package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.dto.postDto.PostRequestDto;
import com.friendfinder.friendfindercommon.dto.postDto.PostResponseDto;
import com.friendfinder.friendfindercommon.entity.FriendRequest;
import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.mapper.PostMapper;
import com.friendfinder.friendfindercommon.repository.PostRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.UserActivityService;
import com.friendfinder.friendfindercommon.service.impl.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.friendfinder.friendfinderrest.util.TestUtil.createPost;
import static com.friendfinder.friendfinderrest.util.TestUtil.mockCurrentUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CurrentUser currentUser;
    @Mock
    private UserRepository userRepository;

    @Mock
    private FriendRequestService friendRequestService;

    @Mock
    private PostMapper postMapper;

    @Mock
    private UserActivityService userActivityService;

    @InjectMocks
    private PostServiceImpl postService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postService = new PostServiceImpl(postRepository, userRepository,
                friendRequestService, postMapper, userActivityService);
        currentUser = mockCurrentUser();
    }

    @Test
    void testPostFindPage() {
        CurrentUser currentUser = mockCurrentUser();
        int pageNumber = 1;
        List<PostResponseDto> allPostFriends = createPostResponseDto();
        when(friendRequestService.findFriendsByUserId(currentUser.getUser().getId())).thenReturn(List.of(currentUser.getUser()));
        when(postMapper.mapResp(any())).thenReturn(allPostFriends);
        List<Integer> friendIds = allPostFriends.stream()
                .map(post -> post.getUser().getId())
                .collect(Collectors.toList());
        Pageable pageable = PageRequest.of(pageNumber - 1, 5, Sort.by(Sort.Order.desc("id")));
        when(postRepository.findByUserIdIn(friendIds, pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(postMapper.mapResp(postRepository.findByUserId(any(Integer.class)))).thenReturn(allPostFriends);
        Page<Post> result = postService.postFindPage(pageNumber, currentUser);
        verify(postRepository).findByUserIdIn(friendIds, pageable);
        assertTrue(result.isEmpty());
    }

    @Test
    void testFindPageVideo() {
        int pageNumber = 1;
        List<PostResponseDto> allPostFriends = createPostResponseDto();

        when(friendRequestService.findFriendsByUserId(currentUser.getUser().getId())).thenReturn(List.of(currentUser.getUser()));
        when(postMapper.mapResp(any())).thenReturn(allPostFriends);
        when(postRepository.findPostsByMusicFileNameIn(anyList(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        when(postMapper.mapResp(postRepository.findByUserId(any(Integer.class)))).thenReturn(allPostFriends);
        Page<Post> resultPage = postService.postFindPageVideo(pageNumber, currentUser);

        verify(postRepository).findPostsByMusicFileNameIn(anyList(), any(Pageable.class));
        assertTrue(resultPage.isEmpty());
    }


    @Test
    void testFindPageImage() {
        int pageNumber = 1;
        List<PostResponseDto> allPostFriends = createPostResponseDto();

        when(friendRequestService.findFriendsByUserId(currentUser.getUser().getId())).thenReturn(List.of(currentUser.getUser()));
        when(postMapper.mapResp(any())).thenReturn(allPostFriends);
        when(postRepository.findPostsByImgNameIn(anyList(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(new ArrayList<>()));

        when(postMapper.mapResp(postRepository.findByUserId(any(Integer.class)))).thenReturn(allPostFriends);
        Page<Post> resultPage = postService.postFindPageImage(pageNumber, currentUser);

        verify(postRepository).findPostsByImgNameIn(anyList(), any(Pageable.class));
        assertTrue(resultPage.isEmpty());
    }


    @Test
     void testPostPageByUserId() {
        int userId = 123;
        int pageNumber = 1;
        List<Post> posts = new ArrayList<>();
        List<Integer> friendIds = posts.stream()
                .map(post -> post.getUser().getId())
                .collect(Collectors.toList());
        when(postRepository.findPostsByUserIdIn(any(), any()))
                .thenReturn(new PageImpl<>(posts));

        Pageable pageable = PageRequest.of(pageNumber - 1, 5, Sort.by(Sort.Order.desc("id")));
        when(postRepository.findPostsByUserIdIn(friendIds, pageable)).thenReturn(new PageImpl<>(new ArrayList<>()));
        Page<Post> resultPage = postService.postPageByUserId(userId, pageNumber);
        assertEquals(posts.size(), resultPage.getContent().size());

    }

    @Test
     void testPostUserById_UserDoesNotExist() {
        int userId = 456;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());
        List<Post> result = postService.postUserById(userId);
        assertTrue(result.isEmpty());
    }

    @Test
    void testPostSaveWithImage() {
        MultipartFile image = mock(MultipartFile.class);
        MultipartFile video = mock(MultipartFile.class);
        String imgName = "image.jpg";
        String musicFileName = null;
        PostRequestDto requestDto = new PostRequestDto();
        requestDto.setDescription("Post with an image");

        Post savedPost = new Post();
        savedPost.setId(1);
        savedPost.setImgName(imgName);

        when(postMapper.map(any(PostRequestDto.class))).thenReturn(savedPost);
        when(postRepository.save(savedPost)).thenReturn(savedPost);

        Post result = postService.postSave(requestDto, currentUser, image, video);

        assertNotNull(result);
        assertEquals(savedPost.getId(), result.getId());
        assertEquals(savedPost.getImgName(), result.getImgName());
        assertNull(result.getMusicFileName());
    }

    @Test
    void testPostSaveWithVideo() {
        MultipartFile image = mock(MultipartFile.class);
        MultipartFile video = mock(MultipartFile.class);
        String imgName = null;
        String musicFileName = "video.mp4";
        PostRequestDto requestDto = new PostRequestDto();
        requestDto.setDescription("Post with a video");

        Post savedPost = new Post();
        savedPost.setId(1);
        savedPost.setMusicFileName(musicFileName);

        when(postMapper.map(any(PostRequestDto.class))).thenReturn(savedPost);
        when(postRepository.save(savedPost)).thenReturn(savedPost);

        Post result = postService.postSave(requestDto, currentUser, image, video);

        assertNotNull(result);
        assertEquals(savedPost.getId(), result.getId());
        assertNull(result.getImgName());
        assertEquals(savedPost.getMusicFileName(), result.getMusicFileName());
    }

    @Test
    void testPostSaveWithoutMedia() {
        MultipartFile image = null;
        MultipartFile video = null;
        PostRequestDto requestDto = new PostRequestDto();
        requestDto.setDescription("Post without media");

        Post savedPost = new Post();
        savedPost.setId(1);

        when(postMapper.map(any(PostRequestDto.class))).thenReturn(savedPost);
        when(postRepository.save(savedPost)).thenReturn(savedPost);

        Post result = postService.postSave(requestDto, currentUser, image, video);

        assertNotNull(result);
        assertEquals(savedPost.getId(), result.getId());
        assertNull(result.getImgName());
        assertNull(result.getMusicFileName());
    }

    @Test
    void testFindAll() {
        List<Post> allPosts = new ArrayList<>();
        Post post1 = new Post();
        post1.setId(1);
        Post post2 = new Post();
        post2.setId(2);
        allPosts.add(post1);
        allPosts.add(post2);

        when(postRepository.findAll()).thenReturn(allPosts);

        List<Post> result = postService.findAll();

        assertNotNull(result);
        assertEquals(allPosts.size(), result.size());
        assertEquals(post1.getId(), result.get(0).getId());
        assertEquals(post2.getId(), result.get(1).getId());
    }

    @Test
    void testPostUserById() {
        int userId = 1;
        User user = new User();
        user.setId(userId);
        Post post1 = new Post();
        post1.setId(1);
        post1.setUser(user);
        Post post2 = new Post();
        post2.setId(2);
        post2.setUser(user);
        List<Post> userPosts = List.of(post1, post2);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(postRepository.findByUserId(userId)).thenReturn(userPosts);
        List<Post> result = postService.postUserById(userId);
        assertNotNull(result);
        assertEquals(userPosts.size(), result.size());
        assertEquals(post1.getId(), result.get(0).getId());
        assertEquals(post2.getId(), result.get(1).getId());
    }

    @Test
     void testDeletePostId() {
        int postId = 1;
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        Post existingPost = createPost();
        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        Post deletedPost = postService.deletePostId(postId);

        assertNull(deletedPost, "Deleted post should be null for non-existing ID.");
        verify(postRepository, never()).deleteById(postId);
    }


    private List<PostResponseDto> createPostResponseDto() {
        FriendRequest friend = createFriend();
        PostResponseDto post1 = PostResponseDto.builder()
                .description("barev")
                .imgName("image.jpg")
                .musicFileName("video.mp4")
                .postDatetime(new Date())
                .user(friend.getReceiver())
                .build();
        PostResponseDto post2 = PostResponseDto.builder()
                .description("barev")
                .imgName("image.jpg")
                .musicFileName("video.mp4")
                .postDatetime(new Date())
                .user(friend.getSender())
                .build();
        return List.of(post1, post2);
    }

    private FriendRequest createFriend() {
        CurrentUser currentUser = mockCurrentUser();
        return FriendRequest.builder()
                .id(1)
                .sender(currentUser.getUser())
                .receiver(currentUser.getUser())
                .status(FriendStatus.PENDING)
                .build();

    }


}