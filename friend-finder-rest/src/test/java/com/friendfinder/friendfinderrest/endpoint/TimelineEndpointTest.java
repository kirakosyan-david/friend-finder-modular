package com.friendfinder.friendfinderrest.endpoint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendfinder.friendfindercommon.dto.userDto.UserUpdateRequestDto;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class TimelineEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @AfterEach
    void cleanRepos() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testEditBasic() throws Exception {
        User currentUser = currentUser();

        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
                .personalInformation("a")
                .city("a")
                .country(new Country(2, "Aland Islands"))
                .dateOfBirth(new Date(2000, 10, 10))
                .gender(UserGender.MALE)
                .name("a")
                .surname("a")
                .email("a")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/timeline/edit-basic").with(user(new CurrentUser(currentUser)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(requestDto))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(requestDto.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.surname").value(requestDto.getSurname()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.personalInformation").value(requestDto.getPersonalInformation()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value(requestDto.getCountry()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value(requestDto.getGender().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(requestDto.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.city").value(requestDto.getCity()));
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testChangeProfilePic() throws Exception {
        User currentUser = currentUser();

        byte[] profilePicData = "Dummy profile pic data".getBytes();

        MockMultipartFile profilePic = new MockMultipartFile(
                "profilePic",
                "profilePic.jpg",
                "image/jpeg",
                profilePicData
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/timeline/change-profile-pic")
                        .file(profilePic)
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        })
                        .with(user(new CurrentUser(currentUser)))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testChangeProfileBackgroundPic() throws Exception {
        User currentUser = currentUser();

        byte[] profilePicData = "Dummy profile pic data".getBytes();

        MockMultipartFile image = new MockMultipartFile(
                "image",
                "image.jpg",
                "image/jpeg",
                profilePicData
        );

        mockMvc.perform(MockMvcRequestBuilders.multipart("/timeline/change-profile-bg-pic")
                        .file(image)
                        .with(request -> {
                            request.setMethod("POST");
                            return request;
                        })
                        .with(user(new CurrentUser(currentUser)))
                )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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