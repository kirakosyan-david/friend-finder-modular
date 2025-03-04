package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.entity.Comment;
import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.Post;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.repository.CommentRepository;
import com.friendfinder.friendfindercommon.repository.PostRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class AdminEndpointTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    public void after() {
        userRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com", authorities = {"ADMIN"})
    void deleteUserById() throws Exception {
        currentUser();

        User user = mockUser();

        Optional<User> optionalUser = userRepository.findByEmail(user.getEmail());
        assertTrue(optionalUser.isPresent());

        mockMvc.perform(delete("/admin/users/delete/" + user.getId()))
                .andExpect(status().isOk());

        assertTrue(userRepository.findByEmail(user.getEmail()).isEmpty());
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com", authorities = {"ADMIN"})
    void deletePostById() throws Exception {
        currentUser();

        Post post = mockPost();
        int postId = post.getId();

        mockMvc.perform(delete("/admin/posts/delete/" + postId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com", authorities = {"ADMIN"})
    void deleteCommentById() throws Exception {
        currentUser();

        User userSave = mockUser();
        Post postSave = mockPost();

        Comment comment = commentRepository.save(Comment.builder()
                .id(1)
                .user(userSave)
                .commentaryText("a")
                .datetime(LocalDateTime.now())
                .post(postSave)
                .build());

        int commentId = comment.getId();

        mockMvc.perform(delete("/admin/comments/delete/" + commentId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com", authorities = {"ADMIN"})
    void blockUserById() throws Exception {
        currentUser();

        User user = mockUser();
        int userId = user.getId();

        mockMvc.perform(put("/admin/users/block/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string("user with id {" + userId + "} successfully blocked"));
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com", authorities = {"ADMIN"})
    void unblockUserById() throws Exception {
        currentUser();

        User user = mockUser();
        int userId = user.getId();

        mockMvc.perform(put("/admin/users/unblock/" + userId))
                .andExpect(status().isOk())
                .andExpect(content().string("user with id {" + userId + "} successfully unblocked"));
    }

    private void currentUser() {
        userRepository.save(User.builder()
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

    private User mockUser() {
        return userRepository.save(User.builder()
                .id(2)
                .name("user")
                .surname("user")
                .email("test@user.com")
                .password("user")
                .dateOfBirth(new Date(1990, 5, 15))
                .gender(UserGender.MALE)
                .city("New York")
                .country(new Country(1, "Afghanistan"))
                .personalInformation("Some personal info")
                .enabled(true)
                .role(UserRole.USER)
                .build());
    }


    private Post mockPost() {
        return postRepository.save(Post.builder()
                .id(2)
                .description("Sample post description")
                .imgName("sample.jpg")
                .musicFileName("sample.mp3")
                .likeCount(0)
                .dislikeCount(0)
                .postDatetime(new Date(2000, 10, 10))
                .user(mockUser())
                .build());
    }
}