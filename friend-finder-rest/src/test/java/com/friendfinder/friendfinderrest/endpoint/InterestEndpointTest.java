package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.repository.InterestsRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class InterestEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private InterestsRepository interestsRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void cleanRepo(){
        interestsRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testInterestsAdd() throws Exception {
        User currentUser = currentUser();

        String interest = "Coding";

        ResultActions result = mockMvc.perform(post("/interests/add").with(user(new CurrentUser(currentUser)))
                .param("interest", interest)
        );

        result.andExpect(status().isOk())
                .andExpect(content().string("interest added to user"));
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
                .role(UserRole.ADMIN)
                .build());
    }
}