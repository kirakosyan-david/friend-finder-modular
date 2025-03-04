package com.friendfinder.friendfindercommon.service.impl;

import com.friendfinder.friendfindercommon.dto.userDto.UserUpdateRequestDto;
import com.friendfinder.friendfindercommon.entity.Country;
import com.friendfinder.friendfindercommon.entity.User;
import com.friendfinder.friendfindercommon.entity.UserImage;
import com.friendfinder.friendfindercommon.repository.CountryRepository;
import com.friendfinder.friendfindercommon.repository.UserRepository;
import com.friendfinder.friendfindercommon.security.CurrentUser;
import com.friendfinder.friendfindercommon.service.TimelineService;
import com.friendfinder.friendfindercommon.service.UserImageService;
import com.friendfinder.friendfindercommon.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * TimelineServiceImpl is a service class that provides methods for managing user timeline information in the application.
 * </p>
 *
 * <p>Fields:</p>
 * <ul>
 *     <li>countryRepository: The CountryRepository interface used to access and retrieve country-related data from the database.</li>
 *     <li>userRepository: The UserRepository interface used to access and retrieve user-related data from the database.</li>
 *     <li>userImageService: The UserImageService interface used to manage user images in the application.</li>
 *     <li>userProfilePicPath: The file path for storing user profile pictures.</li>
 *     <li>userBgProfilePicPath: The file path for storing user profile background pictures.</li>
 * </ul>
 *
 * <p>Methods:</p>
 * <ul>
 *     <li>findAllCountries(): Retrieves a list of all countries from the database.</li>
 *     <li>updateUser(user, currentUser): Updates the user's profile information based on the provided UserUpdateRequestDto.
 *     The method modifies the user's name, surname, email, date of birth, gender, city, country, and personal information.
 *     The updated user object is then saved to the database.</li>
 *     <li>updateUserProfilePic(profilePic, currentUser, userImage): Updates the user's profile picture and saves the UserImage object.
 *     The method uploads the profile picture file using ImageUtil, associates it with the user, and saves the user object along with the UserImage.</li>
 *     <li>updateUserProfileBackgroundPic(bgPic, currentUser): Updates the user's profile background picture.
 *     The method uploads the background picture file using ImageUtil and saves the updated user object to the database.</li>
 * </ul>
 *
 * <p>Usage:</p>
 * <p>
 * TimelineServiceImpl provides functionality for managing user timeline information in the application. It handles operations
 * related to updating user profile details such as name, surname, email, date of birth, gender, city, country, and personal information.
 * The service also allows users to update their profile pictures and background pictures by uploading image files, which are then
 * saved to the appropriate file paths. The service ensures that user-related data is properly updated and persisted in the database
 * to reflect the changes on the user's timeline. It collaborates with other services such as UserImageService and ImageUtil to perform
 * image-related operations efficiently and enhance the user experience in the social media application.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class TimelineServiceImpl implements TimelineService {

    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final UserImageService userImageService;

    @Value("${user.profile.picture.path}")
    private String userProfilePicPath;

    @Value("${user.profile.background-picture.path}")
    private String userBgProfilePicPath;

    @Override
    public List<Country> findAllCountries() {
        return countryRepository.findAll();
    }


    @Override
    public User updateUser(UserUpdateRequestDto user, CurrentUser currentUser) {
        User loggedInUser = currentUser.getUser();
        loggedInUser.setName(user.getName());
        loggedInUser.setSurname(user.getSurname());
        loggedInUser.setEmail(user.getEmail());
        loggedInUser.setDateOfBirth(user.getDateOfBirth());
        loggedInUser.setGender(user.getGender());
        loggedInUser.setCity(user.getCity());
        loggedInUser.setCountry(user.getCountry());
        loggedInUser.setPersonalInformation(user.getPersonalInformation());
        return userRepository.save(loggedInUser);
    }

    @Override
    public User updateUserProfilePic(MultipartFile profilePic, CurrentUser currentUser, UserImage userImage) {
        User user = currentUser.getUser();
        user.setProfilePicture(ImageUtil.uploadImage(profilePic, userProfilePicPath));
        userImageService.userImageSave(userImage, currentUser);
        return userRepository.save(user);
    }

    @Override
    public User updateUserProfileBackgroundPic(MultipartFile bgPic, CurrentUser currentUser) {
        User user = currentUser.getUser();
        user.setProfileBackgroundPic(ImageUtil.uploadImage(bgPic, userBgProfilePicPath));
        return userRepository.save(user);
    }
}
