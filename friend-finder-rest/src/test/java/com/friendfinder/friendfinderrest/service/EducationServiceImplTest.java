package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.Education;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.types.UserGender;
import com.friendfinder.friendfindercommon.entity.types.UserRole;
import com.friendfinder.friendfindercommon.repository.EducationRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.EducationService;
import com.friendfinder.friendfindercommon.service.impl.EducationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class EducationServiceImplTest {

    private EducationService educationService;
    private EducationRepository educationRepository;

    @BeforeEach
    public void setUp() {
        educationRepository = Mockito.mock(EducationRepository.class);
        educationService = new EducationServiceImpl(educationRepository);
    }

    @Test
    void testSaveEducation() {
        Country country = new Country(1, "Afghanistan");
        User currentUser = User.builder()
                .id(1)
                .name("user")
                .surname("user")
                .email("user1@mail.ru")
                .password("user")
                .dateOfBirth(new Date(1990, 5, 15))
                .gender(UserGender.MALE)
                .city("New York")
                .country(country)
                .personalInformation("Some personal info")
                .enabled(true)
                .role(UserRole.USER)
                .build();

        CurrentUser mockCurrentUser = new CurrentUser(currentUser);

        Education education = Education.builder()
                .edName("testEducation")
                .edDescription("testEducation")
                .edFromDate(13)
                .edToDate(134)
                .user(currentUser)
                .build();

        when(educationRepository.save(any(Education.class))).thenReturn(education);
        Education result = educationService.saveEducation(education, mockCurrentUser);

        verify(educationRepository, times(1)).save(education);
        assertEquals(education, result);
    }
}