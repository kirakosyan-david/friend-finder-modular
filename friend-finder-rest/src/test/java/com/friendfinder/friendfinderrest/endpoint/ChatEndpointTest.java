package com.friendfinder.friendfinderrest.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendfinder.friendfindercommon.dto.chatDto.SendMessageDto;
import com.friendfinder.friendfindercommon.entity.Chat;
import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.repository.ChatRepository;
import com.friendfinder.friendfindercommon.repository.MessageRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class ChatEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private MessageRepository messageRepository;

    @AfterEach
    public void after(){
        userRepository.deleteAll();
        chatRepository.deleteAll();
        messageRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testCreateNewChat_Success() throws Exception {
        User mockCurrentUser = currentUser();
        User mockUser = mockUser();

        mockMvc.perform(get("/chat/create/" + mockUser.getId()).with(user(new CurrentUser(mockCurrentUser))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currentUserId").value(mockCurrentUser.getId()))
                .andExpect(jsonPath("$.anotherUserId").value(mockUser.getId()));
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testCreateNewChat_Conflict() throws Exception {
        User currentUser = currentUser();

        mockMvc.perform(get("/chat/create/" + currentUser.getId()).with(user(new CurrentUser(currentUser))))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testSendMessage_Success() throws Exception {
        Chat mockChat = mockChat();
        User mockUser = mockUser();

        SendMessageDto sendMessageDto = new SendMessageDto(mockChat.getId(), mockUser.getId(), "Hello, user!");

        User currentUser = currentUser();

        mockMvc.perform(post("/chat/send-message").with(user(new CurrentUser(currentUser)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(sendMessageDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.receiverId").value(sendMessageDto.getReceiverId()))
                .andExpect(jsonPath("$.senderId").value(currentUser.getId()))
                .andExpect(jsonPath("$.content").value(sendMessageDto.getContent()));
    }

    private User currentUser() {
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
                .role(UserRole.USER)
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

    private Chat mockChat(){
        return chatRepository.save(Chat.builder()
                .anotherUser(mockUser())
                .currentUser(currentUser())
                .build()
        );
    }
}