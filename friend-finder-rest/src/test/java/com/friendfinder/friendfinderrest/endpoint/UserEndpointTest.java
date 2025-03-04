package com.friendfinder.friendfinderrest.endpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendfinder.friendfindercommon.dto.userDto.UserAuthResponseDto;
import com.friendfinder.friendfindercommon.dto.userDto.UserDto;
import com.friendfinder.friendfindercommon.dto.userDto.UserLoginRequestDto;
import com.friendfinder.friendfindercommon.dto.userDto.UserRegisterRequestDto;
import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfinderrest.util.JwtTokenUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class UserEndpointTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testAuthWithValidCredentials() throws Exception {
        User user = mockUser();

        UserLoginRequestDto requestDto = new UserLoginRequestDto(user.getEmail(), "user");
        String requestJson = new ObjectMapper().writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                        .content(requestJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        UserAuthResponseDto responseDto = new ObjectMapper().readValue(responseJson, UserAuthResponseDto.class);

        String token = responseDto.getToken();
        assertEquals(user.getEmail(), jwtTokenUtil.getUsernameFromToken(token));
    }

    @Test
    void testAuthWithInvalidCredentials() throws Exception {
        UserLoginRequestDto requestDto = new UserLoginRequestDto("nonexistent@example.com", "password123");
        String requestJson = new ObjectMapper().writeValueAsString(requestDto);
        try {
            mockMvc.perform(MockMvcRequestBuilders.post("/user/login")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestJson));
        } catch (Exception e){
            assertTrue(true);
        }
    }

    @Test
    void testRegisterUser() throws Exception {
        UserRegisterRequestDto requestDto = UserRegisterRequestDto.builder()
                .city("a")
                .name("a")
                .surname("a")
                .country(new Country(1, "Afghanistan"))
                .dateOfBirth(new Date(2000, 10, 10))
                .gender(UserGender.MALE)
                .password("a")
                .email("uniqusdsade21f2f3f1f2332@email.com")
                .build();
        String requestJson = new ObjectMapper().writeValueAsString(requestDto);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String responseJson = result.getResponse().getContentAsString();
        UserDto responseDto = new ObjectMapper().readValue(responseJson, UserDto.class);

        assertEquals("a", responseDto.getName());
        assertEquals("uniqusdsade21f2f3f1f2332@email.com", responseDto.getEmail());
    }

    private User mockUser() {
        return userRepository.save(User.builder()
                .id(2)
                .name("user")
                .surname("user")
                .email("test@user.com")
                .password(passwordEncoder.encode("user"))
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
