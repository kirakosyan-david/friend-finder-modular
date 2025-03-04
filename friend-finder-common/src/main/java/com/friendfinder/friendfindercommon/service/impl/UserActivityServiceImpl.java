package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.UserActivity;
import com.friendfinder.friendfindercommon.repository.UserActivityRepository;
import com.friendfinder.friendfindercommon.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * UserActivityServiceImpl is a service class that provides methods for managing user activity information in the application.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>userActivityRepository: The UserActivityRepository interface used to access and retrieve user activity data from the database.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>getAllByUserId(userId): Retrieves a list of user activities associated with the given user ID from the database.
 *     The method returns the latest 4 user activities ordered by date and time in descending order.</li>
 *     <li>save(user, type): Saves a new user activity record in the database based on the provided user and activity type.
 *     The method creates a UserActivity object with the user, activity type, and current date and time, and then saves it to the database.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * UserActivityServiceImpl provides functionality for managing user activity information in the application. It handles operations
 * related to retrieving recent user activities and recording user actions in the social media application. The service allows users
 * to view their latest activities, such as posting a photo, commenting on a post, accepting friend requests, etc.
 * It provides a mechanism to log these activities, associating them with the respective users and their timestamps.
 * The service enhances the user experience by enabling users to track their interactions and actions within the application.
 * The recorded user activities can be displayed in the user's timeline or profile page, offering insights into their engagement
 * and contributions to the social platform.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserActivityServiceImpl implements UserActivityService {

    private final UserActivityRepository userActivityRepository;

    @Override
    public List<UserActivity> getAllByUserId(int userId) {
        Optional<List<UserActivity>> allByUserId = userActivityRepository.findTop4ByUserIdOrderByDateTimeDesc(userId);
        return allByUserId.orElse(null);
    }

    @Override
    public void save(User user, String type) {
        userActivityRepository.save(UserActivity.builder()
                .user(user)
                .type(type)
                .dateTime(LocalDateTime.now())
                .build());
    }
}
