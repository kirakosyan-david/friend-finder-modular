package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.UserImage;
import com.friendfinder.friendfindercommon.repository.UserImageRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.UserImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * UserImageServiceImpl is a service class that provides methods for managing user images in the application.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>userImageRepository: The UserImageRepository interface used to access and retrieve user image data from the database.</li>
 *     <li>userRepository: The UserRepository interface used to access and retrieve user data from the database.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>userImagePageByUserId(userId, pageNumber): Retrieves a page of user images associated with the given user ID from the database.
 *     The method returns 12 user images per page and orders them by ID in descending order.</li>
 *     <li>getUserImageById(userId): Retrieves a list of user images associated with the given user ID from the database.</li>
 *     <li>userImageSave(userImage, currentUser): Saves a new user image record in the database based on the provided user image and current user.
 *     The method creates a UserImage object with the user, image name, and then saves it to the database.</li>
 *     <li>deleteUserImageById(id): Deletes a user image record from the database based on the provided image ID.
 *     The method finds the user image by ID, deletes it from the database, and returns null.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * UserImageServiceImpl provides functionality for managing user images in the application. It handles operations related to
 * retrieving and storing user images associated with their profiles. The service allows users to view their images in a paginated format
 * and provides options for uploading and deleting their images. Users can view and manage their images in their profiles or timeline,
 * enhancing their engagement and personalization within the social media platform. The service also interacts with the UserImageRepository
 * and UserRepository to retrieve and store user image data. By integrating these functionalities, the service facilitates efficient
 * handling of user images and ensures a seamless user experience in managing their visual content.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserImageServiceImpl implements UserImageService {

    private final UserImageRepository userImageRepository;
    private final UserRepository userRepository;

    @Override
    public Page<UserImage> userImagePageByUserId(int userId, int pageNumber) {
        List<UserImage> userImageById = getUserImageById(userId);
        List<Integer> userImageId = new ArrayList<>();
        for (UserImage userImage : userImageById) {
            userImageId.add(userImage.getUser().getId());
        }
        Sort sort = Sort.by(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(pageNumber - 1, 12, sort);
        return userImageRepository.findUserImagesByUserIdIn(userImageId, pageable);
    }

    @Override
    public List<UserImage> getUserImageById(int userId) {
        Optional<User> byId = userRepository.findById(userId);
        if (byId.isPresent()) {
            User user = byId.get();
            return userImageRepository.findByUserId(user.getId());
        }
        return Collections.emptyList();
    }

    @Override
    public void userImageSave(UserImage userImage, CurrentUser currentUser) {
        userImageRepository.save(UserImage.builder()
                .user(currentUser.getUser())
                .imageName(currentUser.getUser().getProfilePicture())
                .build());
    }

    @Override
    public UserImage deleteUserImageById(int id) {
        Optional<UserImage> byId = userImageRepository.findById(id);
        if (byId.isPresent()) {
            UserImage userImage = byId.get();
            userImageRepository.deleteById(userImage.getId());
        }
        return null;
    }
}
