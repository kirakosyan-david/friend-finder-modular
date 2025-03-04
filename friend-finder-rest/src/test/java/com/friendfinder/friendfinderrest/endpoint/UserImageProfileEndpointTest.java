package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.entity.*;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.repository.PostRepository;
import com.friendfinder.friendfindercommon.repository.UserImageRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.FriendRequestService;
import com.friendfinder.friendfindercommon.service.UserImageService;
import com.friendfinder.friendfindercommon.service.UserService;
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

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class UserImageProfileEndpointTest {


    @Autowired
    private MockMvc mockMvc;
    private final FriendRequestService friendRequestService = mock(FriendRequestService.class);

    private final UserImageService userImageService = mock(UserImageService.class);

    private final UserService userService = mock(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserImageRepository userImageRepository;

    @InjectMocks
    private UserImageProfileEndpoint userImageProfileEndpoint;

    @AfterEach
    void cleanRepos() {
        userRepository.deleteAll();
        userImageRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testListByPage() throws Exception {
        User createUser = createUser();
        mockMvc.perform(MockMvcRequestBuilders.get("/users/image/profile/" + createUser.getId() + "/page/1")
                        .with(user(new CurrentUser(createUser))))
                .andExpect(status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testSendRequest() {
        int senderId = 1;
        int receiverId = 2;
        User sender = createMockUser(senderId);
        User receiver = createMockUser(receiverId);
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus(FriendStatus.PENDING);

        when(userService.findUserById(senderId)).thenReturn(Optional.of(sender));
        when(userService.findUserById(receiverId)).thenReturn(Optional.of(receiver));

        when(friendRequestService.save(friendRequest)).thenReturn(friendRequest);

        ResponseEntity<FriendRequest> response = userImageProfileEndpoint.sendRequest(senderId, receiverId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(friendRequest, response.getBody());

        verify(userService, times(1)).findUserById(senderId);
        verify(userService, times(1)).findUserById(receiverId);

        verify(friendRequestService, times(1)).save(friendRequest);
    }


    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testDeleteCommentById()  {

        UserImage image = createImage();

        when(userImageService.deleteUserImageById(1)).thenReturn(image);
        ResponseEntity<UserImage> response = userImageProfileEndpoint.deleteImageById(image.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

   private UserImage createImage(){
       User user = createUser();
       return UserImage.builder()
               .id(1)
                .imageName("image.jpg")
                .user(user)
                .build();
    }

    private User createMockUser(int userId) {
        User user = new User();
        user.setId(userId);
        return user;
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
}