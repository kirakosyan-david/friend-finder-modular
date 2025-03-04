package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.dto.workEduDto.EducationCreateRequestDto;
import com.friendfinder.friendfindercommon.dto.workEduDto.WorkExperiencesCreateRequestDto;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class EducationWorkEndpointTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    @AfterEach
    public void after(){
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testEducationAdd() throws Exception {
        User mockCurrentUser = currentUser();

        EducationCreateRequestDto requestDto = mockEducationDto();

        ResultActions result = mockMvc.perform(post("/profile/work-education/education/add").with(user(new CurrentUser(mockCurrentUser)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestDto))
        );

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.edName").value(requestDto.getEdName()))
                .andExpect(jsonPath("$.edFromDate").value(requestDto.getEdFromDate()))
                .andExpect(jsonPath("$.edToDate").value(requestDto.getEdToDate()))
                .andExpect(jsonPath("$.edDescription").value(requestDto.getEdDescription()));
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void testWorkAdd() throws Exception {
        User mockCurrentUser = currentUser();

        WorkExperiencesCreateRequestDto requestDto = mockWorkExperiencesDto();

        ResultActions result = mockMvc.perform(post("/profile/work-education/work/add").with(user(new CurrentUser(mockCurrentUser)))
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(requestDto))
        );

        result.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.company").value(requestDto.getCompany()))
                .andExpect(jsonPath("$.weCity").value(requestDto.getWeCity()))
                .andExpect(jsonPath("$.weToDate").value(requestDto.getWeToDate()))
                .andExpect(jsonPath("$.weFromDate").value(requestDto.getWeFromDate()))
                .andExpect(jsonPath("$.weDesignation").value(requestDto.getWeDesignation()))
                .andExpect(jsonPath("$.weDescription").value(requestDto.getWeDescription()));
    }

    // Utility method to convert objects to JSON string
    private static String asJsonString(Object obj) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(obj);
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
                .role(UserRole.USER)
                .build());
    }

    private EducationCreateRequestDto mockEducationDto(){
        return EducationCreateRequestDto.builder()
                .edDescription("asd")
                .edFromDate(10)
                .edToDate(20)
                .edName("asd")
                .build();
    }

    private WorkExperiencesCreateRequestDto mockWorkExperiencesDto(){
        return WorkExperiencesCreateRequestDto.builder()
                .company("asd")
                .weCity("asd")
                .weDescription("asd")
                .weDesignation("asd")
                .weFromDate(10)
                .weToDate(20)
                .build();
    }
}
