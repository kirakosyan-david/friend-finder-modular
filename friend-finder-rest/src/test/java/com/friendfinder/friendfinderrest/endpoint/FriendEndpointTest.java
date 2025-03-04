package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.FriendRequest;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.FriendStatus;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.repository.FriendRequestRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class FriendEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @AfterEach
    void cleanRepositories() {
        userRepository.deleteAll();
        friendRequestRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testDeleteFromFriends_Success() throws Exception {
        User sender = currentUser();
        User receiver = mockUser();
        friendRequestRepository.save(
                FriendRequest.builder()
                    .sender(sender)
                    .receiver(receiver)
                    .status(FriendStatus.ACCEPTED)
                    .build());

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/friends/delete")
                        .param("sender", String.valueOf(sender.getId()))
                        .param("receiver", String.valueOf(receiver.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testDeleteFromFriends_Failure() throws Exception {
        User sender = currentUser();
        User receiver = mockUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/friends/delete")
                        .param("sender", String.valueOf(sender.getId()))
                        .param("receiver", String.valueOf(receiver.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    private User currentUser() {
        return userRepository.save(User.builder()
                .id(1)
                .name("user")
                .surname("user")
                .email("user@friendfinder.com")
                .password(passwordEncoder.encode("user"))
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
}
