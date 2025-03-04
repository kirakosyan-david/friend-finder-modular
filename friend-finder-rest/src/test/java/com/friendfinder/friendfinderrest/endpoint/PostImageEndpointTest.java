package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.dto.commentDto.CommentRequestDto;
import com.friendfinder.friendfindercommon.dto.postLikeDto.PostLikeDto;
import com.friendfinder.friendfindercommon.entity.*;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.repository.PostRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.CommentService;
import com.friendfinder.friendfindercommon.service.LikeAndDislikeService;
import com.friendfinder.friendfindercommon.service.PostService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class PostImageEndpointTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PostRepository postRepository;
    private final CommentService commentService = mock(CommentService.class);
    private final LikeAndDislikeService likeAndDislikeService = mock(LikeAndDislikeService.class);
    @InjectMocks
    private PostImageEndpoint postImageEndpoint;


    @AfterEach
    void cleanRepos() {
        userRepository.deleteAll();
        postRepository.deleteAll();
    }
    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testListByPage() throws Exception {
        User createUser = createUser();
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/images/page/1")
                        .with(user(new CurrentUser(createUser))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testAddLike() {
        PostLikeDto postLikeDto = new PostLikeDto();
        CurrentUser currentUser = createCurrentUser();
        Post post = createPost();

        PostLike mockPostLike = new PostLike();
        when(likeAndDislikeService.saveReaction(postLikeDto, currentUser, post)).thenReturn(mockPostLike);

        ResponseEntity<PostLike> response = postImageEndpoint.addLike(postLikeDto, currentUser, post);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPostLike, response.getBody());

        verify(likeAndDislikeService, times(1)).saveReaction(postLikeDto, currentUser, post);
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testAddDislike() {
        PostLikeDto postLikeDto = new PostLikeDto();
        CurrentUser currentUser = createCurrentUser();
        Post post = createPost();

        PostLike mockPostLike = new PostLike();
        when(likeAndDislikeService.saveReaction(postLikeDto, currentUser, post)).thenReturn(mockPostLike);

        ResponseEntity<PostLike> response = postImageEndpoint.addDislike(postLikeDto, currentUser, post);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockPostLike, response.getBody());

        verify(likeAndDislikeService, times(1)).saveReaction(postLikeDto, currentUser, post);
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testAddComment() {
        Comment comments = new Comment();
        CurrentUser currentUser = createCurrentUser();
        Post post = createPost();
        CommentRequestDto comment = createCommentDto();

        when(commentService.addComment(comment, currentUser, post)).thenReturn(comments);
        ResponseEntity<Comment> response = postImageEndpoint.addComment(comment, currentUser, post);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testDeleteCommentById()  {

        Comment comments = createComment();

        when(commentService.deleteComment(1)).thenReturn(comments);
        ResponseEntity<Comment> response = postImageEndpoint.removeComment(comments.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    private CommentRequestDto createCommentDto(){
        User user = createUser();
        Post post = createPost();
        return CommentRequestDto.builder()
                .datetime(LocalDateTime.now())
                .user(user)
                .post(post)
                .commentaryText("This is a test comment.")
                .build();
    }

    private Comment createComment(){
        User user = createUser();
        Post post = createPost();
        return Comment.builder()
                .id(1)
                .datetime(LocalDateTime.now())
                .user(user)
                .post(post)
                .commentaryText("This is a test comment.")
                .build();
    }
    private User createUser() {
        return userRepository.save(User.builder()
                .id(1)
                .name("user")
                .surname("user")
                .email("user@friendfinder.com")
                .password("user")
                .dateOfBirth(new Date(1990, 5, 15))
                .gender(UserGender.MALE)
                .city("New York")
                .country(new Country(1, "Afghanistan"))
                .personalInformation("Some personal info")
                .enabled(true)
                .role(UserRole.ADMIN)
                .build());
    }

    private CurrentUser createCurrentUser() {
        Country country = new Country(1, "Afghanistan");
        User currentUser = User.builder()
                .id(1)
                .name("user")
                .surname("user")
                .email("user1@mail.ru")
                .password("user")
                .dateOfBirth(new Date(1990, 5, 15))
                .gender(UserGender.MALE)
                .city("New York")
                .country(country)
                .personalInformation("Some personal info")
                .enabled(true)
                .role(UserRole.USER)
                .build();
        return new CurrentUser(currentUser);
    }

    private Post createPost() {
        User user = createUser();
        return Post.builder()
                .id(1)
                .description("barev")
                .imgName("image.jpg")
                .musicFileName("video.mp4")
                .postDatetime(new Date())
                .user(user)
                .build();
    }
}