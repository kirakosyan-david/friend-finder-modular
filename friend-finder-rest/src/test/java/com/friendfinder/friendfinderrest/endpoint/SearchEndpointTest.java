package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class SearchEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanRepositories(){
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testListByPageSearch() throws Exception {
        String keyword = "John";
        int currentPage = 1;
        User currentUser = currentUser();

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/search/friend/{pageNumber}", currentPage).with(user(new CurrentUser(currentUser)))
                        .param("keyword", keyword)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
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
}
