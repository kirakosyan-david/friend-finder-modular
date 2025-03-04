package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.entity.Interest;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.repository.InterestsRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.InterestsService;
import com.friendfinder.friendfindercommon.service.impl.InterestsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static com.friendfinder.friendfinderrest.util.TestUtil.mockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class InterestsServiceImplTest {

    private InterestsService interestsService;
    private InterestsRepository interestsRepository;

    @BeforeEach
    public void setUp() {
        interestsRepository = mock(InterestsRepository.class);
        interestsService = new InterestsServiceImpl(interestsRepository);
    }

    @Test
    void testFindAllByUserId() {
        int userId = 1;

        List<Interest> expectedInterests = new ArrayList<>();
        expectedInterests.add(new Interest());
        expectedInterests.add(new Interest());
        expectedInterests.add(new Interest());

        when(interestsRepository.findAllByUserId(userId)).thenReturn(expectedInterests);
        List<Interest> resultInterests = interestsService.findAllByUserId(userId);

        verify(interestsRepository, times(1)).findAllByUserId(userId);
        assertEquals(expectedInterests, resultInterests);
    }

    @Test
    void testInterestSave() {
        User currentUser = mockUser();
        CurrentUser mockCurrentUser = new CurrentUser(currentUser);

        String interest = "Hiking";
        interestsService.interestSave(interest, mockCurrentUser);

        verify(interestsRepository, times(1)).save(any());
    }
}