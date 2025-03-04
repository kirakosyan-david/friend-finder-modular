package com.friendfinder.friendfinderrest.service;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.UserActivity;
import com.friendfinder.friendfindercommon.repository.UserActivityRepository;
import com.friendfinder.friendfindercommon.service.impl.UserActivityServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.friendfinder.friendfinderrest.util.TestUtil.mockUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserActivityServiceTest {
    @Mock
    private UserActivityRepository userActivityRepository;

    @InjectMocks
    private UserActivityServiceImpl userActivityService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userActivityService = new UserActivityServiceImpl(userActivityRepository);
    }

    @Test
    void testGetAllByUserId_ExistingUserActivities() {
        int userId = 123;

        List<UserActivity> userActivities = new ArrayList<>();
        userActivities.add(new UserActivity(1, mockUser(), "Post", LocalDateTime.now().minusDays(1)));
        userActivities.add(new UserActivity(2, mockUser(), "Comment", LocalDateTime.now().minusHours(3)));
        userActivities.add(new UserActivity(3, mockUser(), "Like", LocalDateTime.now().minusMinutes(30)));
        userActivities.add(new UserActivity(4, mockUser(), "Friend Request", LocalDateTime.now().minusSeconds(10)));

        when(userActivityRepository.findTop4ByUserIdOrderByDateTimeDesc(userId)).thenReturn(Optional.of(userActivities));

        List<UserActivity> resultUserActivities = userActivityService.getAllByUserId(userId);

        assertNotNull(resultUserActivities);
        assertEquals(4, resultUserActivities.size());
        assertEquals(userActivities, resultUserActivities);

        verify(userActivityRepository, times(1)).findTop4ByUserIdOrderByDateTimeDesc(userId);
    }


    @Test
    void testSaveUserActivity_Success() {
        // Create test data
        int userId = 789;
        User user = mockUser();
        String type = "Post";

        when(userActivityRepository.save(any(UserActivity.class))).thenAnswer(invocation -> {
            UserActivity savedUserActivity = invocation.getArgument(0);
            assertNotNull(savedUserActivity);
            assertEquals(user, savedUserActivity.getUser());
            assertEquals(type, savedUserActivity.getType());
            assertNotNull(savedUserActivity.getDateTime());
            return savedUserActivity;
        });

        userActivityService.save(user, type);

        verify(userActivityRepository, times(1)).save(any(UserActivity.class));
    }


}
