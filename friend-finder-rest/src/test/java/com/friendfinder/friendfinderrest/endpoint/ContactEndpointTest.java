package com.friendfinder.friendfinderrest.endpoint;

import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.service.impl.MailService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.friendfinder.friendfindercommon.dto.contactDto.ContactFormRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-integration.yml")
class ContactEndpointTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void cleanRepositories() {
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void sendContact_ValidContactForm_ReturnsOk() throws Exception {
        currentUser();

        ContactFormRequestDto contactForm = new ContactFormRequestDto();
        contactForm.setName("John Doe");
        contactForm.setEmail("john.doe@example.com");
        contactForm.setSubject("Test Subject");
        contactForm.setText("Test message");

        ResultActions result = mockMvc.perform(post("/contact/send-contact-form")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(contactForm))
        );

        result.andExpect(status().isOk())
                .andExpect(content().string("Mail Sent!"));
    }

    @Test
    @WithMockUser(username = "user@friendfinder.com")
    void sendContact_InvalidContactForm_ReturnsBadRequest() throws Exception {
        currentUser();

        ContactFormRequestDto contactForm = new ContactFormRequestDto();
        contactForm.setName("name");
        contactForm.setEmail(null); // missing email
        contactForm.setSubject("subject");
        contactForm.setText("text");

        ResultActions result = mockMvc.perform(post("/contact/send-contact-form")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(contactForm))
        );

        result.andExpect(status().isBadRequest());
    }

    private String asJsonString(Object obj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
            .role(UserRole.USER)
            .build());
    }
}
