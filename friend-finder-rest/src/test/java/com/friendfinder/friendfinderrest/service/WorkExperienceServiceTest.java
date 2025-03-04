package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.WorkExperiences;
import com.friendfinder.friendfindercommon.repository.WorkExperiencesRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.impl.WorkExperiencesServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.friendfinder.friendfinderrest.util.TestUtil.mockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkExperienceServiceTest {

    @Mock
    private WorkExperiencesRepository workExperiencesRepository;

    @InjectMocks
    private WorkExperiencesServiceImpl workExperiencesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        workExperiencesService = new WorkExperiencesServiceImpl(workExperiencesRepository);
    }

    @Test
    void testFindAllByUserId() {
        int userId = 123;
        WorkExperiences workExperience1 = mockWE();
        WorkExperiences workExperience2 = mockWE();
        List<WorkExperiences> workExperiencesList = Arrays.asList(workExperience1, workExperience2);

        when(workExperiencesRepository.findAllByUserId(userId)).thenReturn(workExperiencesList);

        List<WorkExperiences> result = workExperiencesService.findAllByUserId(userId);

        assertEquals(workExperiencesList.size(), result.size());
        assertEquals(workExperiencesList, result);

        verify(workExperiencesRepository, times(1)).findAllByUserId(userId);
    }

    @Test
    void testSaveWorkExperiences() {
        CurrentUser currentUser = new CurrentUser(mockUser());
        User user = mockUser();
        WorkExperiences workExperiences = mockWE();

        when(workExperiencesRepository.save(workExperiences)).thenReturn(workExperiences);

        WorkExperiences result = workExperiencesService.saveWorkExperiences(workExperiences, currentUser);

        assertEquals(user, workExperiences.getUser());
        assertEquals(workExperiences, result);

        verify(workExperiencesRepository, times(1)).save(workExperiences);
    }

    private WorkExperiences mockWE(){
        return WorkExperiences.builder()
                .company("a")
                .weCity("a")
                .weDescription("a")
                .weDesignation("a")
                .weFromDate(1943)
                .weToDate(2000)
                .user(mockUser())
                .build();
    }
}
